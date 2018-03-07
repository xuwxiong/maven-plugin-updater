package intact.maven.plugins.bom;

import org.apache.commons.lang.StringUtils;


public enum UpdateBOMDirectionEnum {
	POM("pom","Update pom from Wiki."),
	WIKI("wiki","Update Wiki from pom.");
	
	public static final String VALUES_LIST = "|"+StringUtils.join(UpdateBOMDirectionEnum.values(), "|")+"|";
	
	public static final String CODE_POM = POM.getCode();
	public static final String CODE_WIKI = WIKI.getCode();
	
	private final String code;
	private final String description;
	
	private UpdateBOMDirectionEnum(String code,String description)
	{
		this.code = code;
		this.description = description;
	}

	public static UpdateBOMDirectionEnum fromCode(String pCode)
    {
        if(isValidCode(pCode))
        {
        	return UpdateBOMDirectionEnum.valueOf(pCode.toUpperCase());	
        }
        
        throw new IllegalArgumentException("Unsupported/unknown code: " + pCode);
    }

	public String getCode() {
		return code;
	}
	
	public String getDescription() {
		return description;
	}

	public static boolean isValidCode(String pCode)
	{
		if (null == pCode)
		{
			return false;
		}
		
		return VALUES_LIST.contains("|"+pCode.toUpperCase()+"|");
	}
	
	public static boolean isPOMDirection(String pDirection)
	{
		return CODE_POM.equalsIgnoreCase(pDirection);
	}
	
	public static boolean isWikiirection(String pDirection)
	{
		return CODE_WIKI.equalsIgnoreCase(pDirection);
	}
	
	
}
