package test;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import sun.util.logging.resources.logging;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudfront_2012_03_15.model.transform.LoggingConfigStaxUnmarshaller;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.DescribeRegionsResult;
import com.amazonaws.services.ec2.model.GetConsoleOutputRequest;
import com.amazonaws.services.ec2.model.GetConsoleOutputResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Region;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.Tag;
import com.amazonaws.services.ec2.model.Volume;
import com.amazonaws.services.ec2.model.VolumeAttachment;

public class TestSonar {

	public static void main(String[] args) throws Exception {
		try {

			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("Assets");
			Row row0 = sheet.createRow(0);
			row0.createCell(0).setCellValue("Region");
			row0.createCell(1).setCellValue("Instance");
			row0.createCell(2).setCellValue("PublicIp##PublicDns##PrivateIp##PrivateDns");
			row0.createCell(3).setCellValue("State");
			row0.createCell(4).setCellValue("Name");
			row0.createCell(5).setCellValue("Creation Time");
			row0.createCell(6).setCellValue("Last Action Time");
			row0.createCell(7).setCellValue("Last Action Reason");
			//devvm
			//AWSCredentials credentials = new BasicAWSCredentials("Hashed", "Hashed");
			String user = "Hashed";
			String pass="Hashed";
			AWSCredentials credentials = new BasicAWSCredentials(user,pass);

			ClientConfiguration config = new ClientConfiguration();
			config.setMaxErrorRetry(15);
			config.setUseGzip(true);
			AmazonEC2Client ec2 = new AmazonEC2Client(credentials,config);
			DescribeRegionsResult describeRegions = ec2.describeRegions();
			List<Region> regions = describeRegions.getRegions();
			int rowNum = 1;
			int sDec=0,rDec=0,sJan=0,rJan=0,sFeb=0,rFeb=0,sMar=0,rMar=0,sApr=0,rApr=0,sMay=0,rMay=0,sJun=0,rJun=0,sJul=0,rJul=0,sAug=0,rAug=0
					,sSep=0,rSep=0,sOct=0,rOct=0,sNov=0,rNov=0;

			for (Region region : regions) {

			ec2.setEndpoint(region.getEndpoint());

			Hashtable<String, String> vols = new Hashtable<String, String>();
			List<Volume> volumes = ec2.describeVolumes().getVolumes();
			for (Iterator iterator = volumes.iterator(); iterator.hasNext();) {
				Volume volume = (Volume) iterator.next();
				String volumeId = volume.getVolumeId();
				String VolumeSize = volume.getSize().toString();
				String instanceId = "";
				String attachTime = "";
				String attachDev="";
				ListIterator<VolumeAttachment> listIterator = volume.getAttachments().listIterator();
				while (listIterator.hasNext()) {
					VolumeAttachment attachmentInfo = (VolumeAttachment) listIterator
							.next();
					instanceId = attachmentInfo.getInstanceId();
					//attachTime = attachTime +" , "+attachmentInfo.getAttachTime().toGMTString();
					attachTime = attachmentInfo.getAttachTime().toGMTString();
					attachDev = attachDev +" , "+attachmentInfo.getDevice();
				}
				//vols.put(instanceId, volumeId+" ^ "+VolumeSize+" ^ "+attachTime+" ^ "+attachDev+" ^ ");
				vols.put(instanceId, attachTime);
			}

			DescribeInstancesResult result = ec2.describeInstances();
			List<Reservation> reservations = result.getReservations();

			for (Reservation reservation : reservations) {
				for (Instance instance : reservation.getInstances()) {
					GetConsoleOutputRequest getConsoleOutputRequest = new GetConsoleOutputRequest();
					getConsoleOutputRequest.setInstanceId(instance.getInstanceId());
					GetConsoleOutputResult consoleOutput = ec2.getConsoleOutput(getConsoleOutputRequest );

					String name = "";
					List<Tag> tags = instance.getTags();
					for (Tag tag : tags) {
					if("Name".equals(tag.getKey())){
						name = tag.getValue();
					}
					}
					String reasonState= "";
					try{
						reasonState= instance.getStateReason().getMessage();
					}catch(Exception e){
						//comment this out
						Logger.getGlobal().fine(e.getLocalizedMessage());
					}



					//calculation before Jan
					if(consoleOutput.getTimestamp().compareTo(new Date(2016-1900, 0, 1)) < 0){
						if("running".equals(instance.getState().getName())){
							rDec++;
						}else {
							sDec++;
						}
					}

					//calculation before Feb
					if(consoleOutput.getTimestamp().compareTo(new Date(2016-1900, 1, 1)) < 0){
						if("running".equals(instance.getState().getName())){
							rJan++;
						}else {
							sJan++;
						}
					}

					//calculation before Mar
					if(consoleOutput.getTimestamp().compareTo(new Date(2016-1900, 2, 1)) < 0){
						if("running".equals(instance.getState().getName())){
							rFeb++;
						}else {
							sFeb++;
						}
					}
					//calculation before Apr
					if(consoleOutput.getTimestamp().compareTo(new Date(2016-1900, 3, 1)) < 0){
						if("running".equals(instance.getState().getName())){
							rMar++;
						}else {
							sMar++;
						}
					}

					//calculation before May
					if(consoleOutput.getTimestamp().compareTo(new Date(2016-1900, 5, 1)) < 0){
						if("running".equals(instance.getState().getName())){
							rApr++;
						}else {
							sApr++;
						}
					}

					//calculation before June
					if(consoleOutput.getTimestamp().compareTo(new Date(2016-1900, 6, 1)) < 0){
						if("running".equals(instance.getState().getName())){
							rMay++;
						}else {
							sMay++;
						}
					}

					//calculation before Jul
					if(consoleOutput.getTimestamp().compareTo(new Date(2016-1900, 7, 1)) < 0){
						if("running".equals(instance.getState().getName())){
							rJun++;
						}else {
							sJun++;
						}
					}

					//calculation before Aug
					if(consoleOutput.getTimestamp().compareTo(new Date(2016-1900, 8, 1)) < 0){
						if("running".equals(instance.getState().getName())){
							rJul++;
						}else {
							sJul++;
						}
					}

					//calculation before Spe
					if(consoleOutput.getTimestamp().compareTo(new Date(2016-1900, 9, 1)) < 0){
						if("running".equals(instance.getState().getName())){
							rAug++;
						}else {
							sAug++;
						}
					}

					//calculation before Oct
					if(consoleOutput.getTimestamp().compareTo(new Date(2016-1900, 10, 1)) < 0){
						if("running".equals(instance.getState().getName())){
							rSep++;
						}else {
							sSep++;
						}
					}

					//calculation before Nov
					if(consoleOutput.getTimestamp().compareTo(new Date(2016-1900, 11, 1)) < 0){
						if("running".equals(instance.getState().getName())){
							rOct++;
						}else {
							sOct++;
						}
					}

					//calculation before Dec
					if(consoleOutput.getTimestamp().compareTo(new Date(2016-1900, 12, 1)) < 0){
						if("running".equals(instance.getState().getName())){
							rNov++;
						}else {
							sNov++;
						}
					}


					System.out.println(region.getRegionName() + " ~ "+ instance.getInstanceId()+ " ~ "+instance.getState().getName()+" ~ "
				+name+" ~ "+vols.get(instance.getInstanceId()) + " ~ "+ consoleOutput.getTimestamp().toGMTString()+" ~ "+reasonState );

					Row row = sheet.createRow(rowNum);
					rowNum++;
					row.createCell(0).setCellValue(region.getRegionName());
					row.createCell(1).setCellValue(instance.getInstanceId());
					row.createCell(2).setCellValue(instance.getPublicIpAddress()+"##"+instance.getPublicDnsName()+"##"+instance.getPrivateIpAddress()+"##"+instance.getPrivateDnsName());
					row.createCell(3).setCellValue(instance.getState().getName());
					row.createCell(4).setCellValue(name);
					row.createCell(5).setCellValue(vols.get(instance.getInstanceId()));
					row.createCell(6).setCellValue(consoleOutput.getTimestamp().toGMTString());
					row.createCell(7).setCellValue(reasonState);
				}
			}
		}

			Row row = sheet.createRow(rowNum);
			rowNum++;
			row.createCell(0).setCellValue("Month");
			row.createCell(1).setCellValue("Dec");
			row.createCell(2).setCellValue("Jan");
			row.createCell(3).setCellValue("Feb");
			row.createCell(4).setCellValue("Mar");
			row.createCell(5).setCellValue("Apr");
			row.createCell(6).setCellValue("May");
			row.createCell(7).setCellValue("Jun");
			row.createCell(8).setCellValue("Jul");
			row.createCell(9).setCellValue("Aug");
			row.createCell(10).setCellValue("Sep");
			row.createCell(11).setCellValue("Oct");
			row.createCell(12).setCellValue("Nov");

			row = sheet.createRow(rowNum);
			rowNum++;
			row.createCell(0).setCellValue("stopped");
			row.createCell(1).setCellValue(sDec);
			row.createCell(2).setCellValue(sJan);
			row.createCell(3).setCellValue(sFeb);
			row.createCell(4).setCellValue(sMar);
			row.createCell(5).setCellValue(sApr);
			row.createCell(6).setCellValue(sMay);
			row.createCell(7).setCellValue(sJun);
			row.createCell(8).setCellValue(sJul);
			row.createCell(9).setCellValue(sAug);
			row.createCell(10).setCellValue(sSep);
			row.createCell(11).setCellValue(sOct);
			row.createCell(12).setCellValue(sNov);

			row = sheet.createRow(rowNum);
			rowNum++;
			row.createCell(0).setCellValue("running");
			row.createCell(1).setCellValue(rDec);
			row.createCell(2).setCellValue(rJan);
			row.createCell(3).setCellValue(rFeb);
			row.createCell(4).setCellValue(rMar);
			row.createCell(5).setCellValue(rApr);
			row.createCell(6).setCellValue(rMay);
			row.createCell(7).setCellValue(rJun);
			row.createCell(8).setCellValue(rJul);
			row.createCell(9).setCellValue(rAug);
			row.createCell(10).setCellValue(rSep);
			row.createCell(11).setCellValue(rOct);
			row.createCell(12).setCellValue(rNov);


			try (FileOutputStream outputStream = new FileOutputStream("E:/DevVM-All Nov 22-Summary.xls")) {
	            workbook.write(outputStream);
	        }

		} catch (Exception e) {
			e.printStackTrace();
			throw e;

		}
	}
}

/*
git add *
git commit -m "comment"
git push
E:\programs\sonar-scanner-3.0.1.733-windows\bin\sonar-scanner.bat
*/