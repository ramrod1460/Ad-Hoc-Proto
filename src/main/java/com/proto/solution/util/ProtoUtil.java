package com.proto.solution.util;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.util.Arrays;

import com.proto.solution.HeaderData;
import com.proto.solution.RecordData;
import com.proto.solution.constants.ProtoConstants;
import com.proto.solution.exception.BadFileException;

/**
 * Utility class containing driver classes and utilities
 * 
 * @author User
 *
 */
@SuppressWarnings({"PMD.CommentRequired","PMD.CommentSize","PMD.LongVariable","PMD.AvoidDuplicateLiterals","PMD.UseUtilityClass","PMD.SystemPrintln","PMD.AvoidPrefixingMethodParameters"})
public class ProtoUtil {

	/**
	 * 
	 * Process header record from the binary log file
	 * 
	 * @param inStream binary log file
	 * @return HeaderData object record
	 * 
	 * @throws IOException
	 * @throws EOFException
	 */
	@SuppressWarnings({"PMD.LawOfDemeter"})
	public static HeaderData processHeader(final InputStream inStream) throws IOException, EOFException, BadFileException {
		// Header
		// | 4 bytes magic | | 1 byte version | 4 byte recordCount 

		final byte[] header = new byte[ProtoConstants.HEADER_LENGTH];
		inStream.read(header);

		final String magic = new String(Arrays.copyOfRange(header, 0, 4));
		
		// Ensure we have a valid binary file
		if ( ! magic.equalsIgnoreCase(ProtoConstants.MAGIC_STRING)) {
			throw new BadFileException("Binary input file specified does not appear to be a valid file ...magic string is not "+ProtoConstants.MAGIC_STRING+"...it is "+magic);
		}
		final byte version = header[4];
		final int count = ByteBuffer.wrap( Arrays.copyOfRange(header, 5, 9)).getInt();

		if ( count < 0) {
			throw new BadFileException("Binary input file specified does not appear to be a valid file ...record count is negative "+count);
		}
		
		return new HeaderData(magic, version, count);

	}

	/**
	 * Process record ( versus header ) data from the binary log file
	 * 
	 * @param inStream binary log file
	 * @return RecordData object record
	 * @throws IOException
	 * @throws EOFException
	 */
	@SuppressWarnings({"PMD.DataflowAnomalyAnalysis","PMD.LawOfDemeter"})
	public static RecordData processRecordData(final InputStream inStream) throws IOException, EOFException {
		// Record
		// | 1 byte record type enum | 4 byte (uint32) Unix timestamp | 8 byte (uint64) user ID 
		// | 8 byte amount at end if debit or credit
		
		byte buffer [] = new byte[1];
		inStream.read(buffer);
		final byte recordOfType = buffer[0];
		
		buffer = new byte[4];
		inStream.read(buffer);
		final int timeStamp = ByteBuffer.wrap(buffer).getInt();
		
		final byte user[] = new byte[8];
		inStream.read(user);
		final String userId = Long.toUnsignedString(byteArrayToLong(user));

		double amount = 0;
		if ( ProtoConstants.DEBIT == recordOfType || ProtoConstants.CREDIT == recordOfType ) {
			final byte amountArray [] = new byte[8];
			inStream.read(amountArray);
			amount = ByteBuffer.wrap(amountArray).getDouble();
		}

		return new RecordData(recordOfType, timeStamp, userId, BigDecimal.valueOf(amount));

	}

	/**
	 * Controller level method for processing Header and Record level data from binary log
	 * 
	 * @param inStream binary log file
	 * @throws IOException
	 * @throws EOFException
	 */
	@SuppressWarnings({"PMD.DataflowAnomalyAnalysis","PMD.LawOfDemeter"})
	public static void parse(final InputStream inStream) throws IOException, BadFileException, EOFException {

		// Process header
		int recordsToProcess = processHeader(inStream).getRecordCount();
		
		// Process individual records
		BigDecimal totalCreditScaled = BigDecimal.ZERO;
		BigDecimal totalDebitScaled = BigDecimal.ZERO;
		BigDecimal magicUserBalanceDebit = BigDecimal.ZERO;
		BigDecimal magicUserBalanceCredit = BigDecimal.ZERO;
		
		// Autopay counts
		int startAutopay = 0;
		int endAutopay = 0;
		
		do {
			final RecordData record = processRecordData(inStream);

			// accumulate debits / credits / autopays
			if ( Byte.compare(record.getTypeOfRecord(), ProtoConstants.DEBIT) == 0 ) {
				if ( record.getUsedId().matches(ProtoConstants.MAGIC_USER) ) {
					magicUserBalanceDebit = magicUserBalanceDebit.add(record.getAmount());
				}
				totalDebitScaled = totalDebitScaled.add(record.getAmount());
			} else if ( Byte.compare(record.getTypeOfRecord(), ProtoConstants.CREDIT) == 0) {
				if ( record.getUsedId().matches(ProtoConstants.MAGIC_USER) ) {
					magicUserBalanceCredit = magicUserBalanceCredit.add(record.getAmount());
				}
				totalCreditScaled = totalCreditScaled.add(record.getAmount());
			} else if ( Byte.compare(record.getTypeOfRecord(), ProtoConstants.START_AUTOPAY) == 0 ) {
				startAutopay++;
			} else if ( Byte.compare(record.getTypeOfRecord(),ProtoConstants.END_AUTOPAY) == 0 ) {
				endAutopay++;
			}

		} while (--recordsToProcess >= 0);

		System.out.println("total credit amount=" + totalCreditScaled.setScale(2, RoundingMode.HALF_UP));
		System.out.println("total debit amount=" + totalDebitScaled.setScale(2, RoundingMode.HALF_UP));
		System.out.println("autopays started=" + startAutopay);
		System.out.println("autopays ended=" + endAutopay);
		System.out.println("balance for user 2456938384156277127="+ magicUserBalanceCredit.subtract(magicUserBalanceDebit).setScale(2, RoundingMode.HALF_UP));
		
	}

	/**
	 * Support method for long type byte operations
	 * 
	 * @param array - byte array upon which to be operated
	 * @return long value
	 */
	@SuppressWarnings({"PMD.LawOfDemeter"})
	public static long byteArrayToLong(final byte[] array) {
        return ByteBuffer.wrap(array).getLong();
    }
	
}
