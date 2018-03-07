<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html"/>
    <xsl:template match="/project-configuration">
        <html>
            <head>
                <title>Project Configuration DOM Version <xsl:value-of select="name"/></title>
            </head>
            <body>
                <p><strong>DOM Version <xsl:value-of select="name"/></strong>
                    <xsl:if test="parent"> (coming from parent DOM <xsl:value-of select="parent"
                        />)</xsl:if></p>
                <table border="1" cellspacing="0" cellpadding="3">
                    <tr>
                        <td><strong>GroupId</strong></td>
                        <td><strong>ArtifactId</strong></td>
                        <td><strong>Version</strong></td>
                        <td><strong>Note</strong></td>
                    </tr>
                    <xsl:for-each select="dependencies/dependency">
                        <xsl:variable name="tr-style">
                            <xsl:choose>
                                <xsl:when test="position() mod 2 = 0"/>
                                <xsl:otherwise>background-color:#CEE3F6</xsl:otherwise>
                            </xsl:choose>
                        </xsl:variable>

                        <tr style="{$tr-style}">
                            <td>
                                <xsl:value-of select="groupId"/>
                            </td>
                            <td>
                                <xsl:value-of select="artifactId"/>
                            </td>
                            <td>
                                <xsl:value-of select="version"/>
                            </td>
                            <td>
                                <xsl:choose>
                                    <xsl:when test="not(note)">&#160;</xsl:when>
                                    <xsl:otherwise>
                                        <xsl:value-of select="note"/>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </td>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>
