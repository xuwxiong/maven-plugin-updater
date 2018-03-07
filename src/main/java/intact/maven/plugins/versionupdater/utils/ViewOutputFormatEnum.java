package intact.maven.plugins.versionupdater.utils;

import intact.maven.plugins.versionupdater.dependencies.model.IPrintableSelection;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.digester3.Digester;
import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


public enum ViewOutputFormatEnum {
	XML("xml","",true,"XML Format.",true,"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"),
	XML_BOM("xml","bom",true,"XML Format.",true,"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"),
	XML_RELEASE("xml","release",true,"XML Format.",true,"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"),
	XML_BOM_RELEASE("xml","bom_release",true,"XML Format.",true,"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"),
	JSON("json","",false,"JSON Format.",false,""),
	JSON_BOM("json","bom",false,"JSON Format.",false,""),
	JSON_RELEASE("json","release",false,"JSON Format.",false,""),
	JSON_BOM_RELEASE("json","bom_release",false,"JSON Format.",false,"");
	
	public static final String CODE_XML = XML.getCode();
	public static final String CODE_JSON = JSON.getCode();
	
	public static final String PURPOSE_BOM = JSON_BOM.getPurpose();
	public static final String PURPOSE_RELEASE = JSON_RELEASE.getPurpose();
	public static final String PURPOSE_BOM_RELEASE = JSON_BOM_RELEASE.getPurpose();
	
	public static final String VALUES_LIST = StringUtils.join(ViewOutputFormatEnum.values(), "");

	private static final String PATTERN_TO_STRING = "(%s,%s)";
	
	private static final String PATTERN_ENUM_VALUE = "%s_%s";
	
	private static final PrinterXML PRINTER_XML = new PrinterXML(ContentViews.Release.class);
	private static final PrinterXML PRINTER_XML_BOM = new PrinterXML(ContentViews.Bom.class);
	private static final PrinterXML PRINTER_XML_BOM_RELEASE = new PrinterXML(ContentViews.BomRelease.class);
	private static final PrinterXML PRINTER_XML_RELEASE = new PrinterXML(ContentViews.Release.class);
	private static final PrinterJSON PRINTER_JSON_RELEASE = new PrinterJSON(ContentViews.Release.class);
	private static final PrinterJSON PRINTER_JSON_BOM = new PrinterJSON(ContentViews.Bom.class);
	private static final PrinterJSON PRINTER_JSON_BOM_RELEASE = new PrinterJSON(ContentViews.BomRelease.class);
	private static final PrinterAsIs PRINTER_DEFAULT = new PrinterAsIs();
	
	private static final Map<ViewOutputFormatEnum,PrinterFormat> PRINTERS_MAPPING = new HashMap<ViewOutputFormatEnum, ViewOutputFormatEnum.PrinterFormat>();
	
	static
	{
		PRINTERS_MAPPING.put(XML, PRINTER_XML);
		PRINTERS_MAPPING.put(XML_BOM,PRINTER_XML_BOM);
		PRINTERS_MAPPING.put(XML_RELEASE,PRINTER_XML_RELEASE);
		PRINTERS_MAPPING.put(XML_BOM_RELEASE,PRINTER_XML_BOM_RELEASE);
		PRINTERS_MAPPING.put(JSON_BOM,PRINTER_JSON_BOM);
		PRINTERS_MAPPING.put(JSON_RELEASE,PRINTER_JSON_RELEASE);
		PRINTERS_MAPPING.put(JSON_BOM_RELEASE,PRINTER_JSON_BOM_RELEASE);
	}	
	
	private final String code;
	private final String purpose;
	private final boolean ignorePurpose;
	private final String description;
	private final boolean headerNeeded;
	private final String header;
	
	private ViewOutputFormatEnum(String pCode,String pPurpose,boolean pIgnorePurpose,String pDescription,boolean pHeaderNeeded,String pHeader)
	{
		this.code = pCode;
		this.purpose = pPurpose;
		this.ignorePurpose = pIgnorePurpose;
		this.description = pDescription;
		this.headerNeeded = pHeaderNeeded;
		this.header = pHeader;
	}

	public static ViewOutputFormatEnum fromCodeAndPurpose(String pCode,String pPurpose)
    {
		ViewOutputFormatEnum resultEnum = null;
		
    	String enumName = null;
    	
    	if(StringUtils.isNotBlank(pPurpose))
    	{
    		enumName = String.format(PATTERN_ENUM_VALUE, pCode.toUpperCase(),pPurpose.toUpperCase());	
    	}
    	else
    	{
    		enumName = pCode.toUpperCase();
    	}
    	
    	try
    	{
    		resultEnum = valueOf(enumName);
    	}
    	catch(IllegalArgumentException e)
    	{
        	try
        	{
        		resultEnum = valueOf(pCode.toUpperCase());
        	}
        	catch(IllegalArgumentException e1)
        	{
        		
        	}    		
    	}
    	
    	return resultEnum;
    }

	public String getCode() {
		return code;
	}	
	
	public String getPurpose() {
		return purpose;
	}

	public boolean isIgnorePurpose() {
		return ignorePurpose;
	}

	public String getDescription() {
		return description;
	}	

	public boolean isHeaderNeeded() {
		return headerNeeded;
	}	

	public String getHeader() {
		return header;
	}

	public static boolean isValidEnum(String pCode,String pPurpose)
	{
		if ((null == pCode) || (null == pPurpose))
		{
			return false;
		}
		
		boolean resultFlag = VALUES_LIST.contains(String.format(PATTERN_TO_STRING, pCode.toLowerCase(),pPurpose.toLowerCase()));
		
		if(!resultFlag)
		{
			resultFlag = VALUES_LIST.contains(String.format(PATTERN_TO_STRING, pCode.toLowerCase(),""));
		}
		
		return resultFlag;
	}
	
	public String printJSONFormat(Object pObj,Class<? extends ContentViews.Normal> pViewClass) throws Exception
	{		
		PrinterJSON jsconPrinter = new PrinterJSON(pViewClass);

		return jsconPrinter.print(pObj);
	}
	
	public String printFormat(Object pObj) throws Exception
	{		
		PrinterFormat printer = PRINTERS_MAPPING.get(this);
		
		if(null == printer)
		{
			printer = PRINTER_DEFAULT;
		}

		return printer.print(pObj);
	}
	
	public <T> T read(File pFile, Class<T> pClass) throws Exception
	{		
		PrinterFormat printer = PRINTERS_MAPPING.get(this);
		
		if(null == printer)
		{
			printer = PRINTER_DEFAULT;
		}

		return printer.read(pFile,pClass);
	}
	
	public static boolean isXMLFormat(String pFormat)
	{
		return CODE_XML.equalsIgnoreCase(pFormat);
	}
	
	public static boolean isJSONFormat(String pFormat)
	{
		return CODE_JSON.equalsIgnoreCase(pFormat);
	}
	
	static interface PrinterFormat
	{
		String print(Object pObj) throws Exception;
		<T> T read(File pFile, Class<T> pClass) throws Exception;
	}

	static class PrinterAsIs implements PrinterFormat
	{
		@Override
		public String  print(Object pObj)  throws Exception
		{
			return ((null != pObj) ? pObj.toString():null);
		}

		@Override
		public <T> T read(File pFile, Class<T> pClass) throws Exception 
		{
			return null;
		}
	}
	
	static class PrinterXML implements PrinterFormat
	{
		private static final Digester digester= DigesterUtils.getDigester();
		
		private final Class<? extends ContentViews.Normal> viewClass;
		
		public PrinterXML(Class<? extends ContentViews.Normal> pViewClass)
		{
			this.viewClass = pViewClass;
		}
		
		@Override
		public String  print(Object pObj)  throws Exception
		{
			String resultStr = null;
			
			if (pObj instanceof IPrintableSelection)
			{
				IPrintableSelection printabaleSelectObj = (IPrintableSelection) pObj;
				
				String[] fieldsSelection = Utils.getFieldsWithAnnotation(pObj, viewClass);
				
				resultStr = printabaleSelectObj.toString(fieldsSelection);
			}
			else
			{
				resultStr = ((null != pObj) ? pObj.toString():null);
			}			
			
			 return resultStr;
		}

		@Override
		public <T> T read(File pFile, Class<T> pClass) throws Exception 
		{
			return digester.parse(pFile);
		}	
	}
	
	static class PrinterJSON implements PrinterFormat
	{		
		private static final ObjectMapper jsonMapper = new ObjectMapper();
		
		static
		{
			jsonMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
		}
		
		private final Class<? extends ContentViews.Normal> viewClass;
		
		public PrinterJSON(Class<? extends ContentViews.Normal> pViewClass)
		{
			this.viewClass = pViewClass;
		}
		
		@Override
		public String  print(Object pObj)  throws Exception
		{
			 return jsonMapper.writerWithView(viewClass).writeValueAsString(pObj);
		}

		@Override
		public <T> T read(File pFile, Class<T> pClass) throws Exception 
		{
			return jsonMapper.readerWithView(viewClass).forType(pClass).readValue(pFile);
		}	
	}

	@Override
	public String toString() 
	{	
		return String.format(PATTERN_TO_STRING, getCode(),getPurpose());
	}
	
	public static void main(String[] args) {
		ViewOutputFormatEnum valEnum = ViewOutputFormatEnum.fromCodeAndPurpose("xml","");
		
		System.out.println(valEnum);
		
		valEnum = ViewOutputFormatEnum.fromCodeAndPurpose("json","bom");
		
		System.out.println(valEnum);
		
		valEnum = ViewOutputFormatEnum.fromCodeAndPurpose("json","release");
		
		System.out.println(valEnum);
		
		valEnum = ViewOutputFormatEnum.fromCodeAndPurpose("xml","");
		
		System.out.println(valEnum);
	}
}
