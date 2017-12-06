<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/">
        <html>
            <body style="font-family: Verdana, sans-serif;">
                <div style="margin: 0 100px; border: 1px solid black;">
                    <h1 style="text-align: center; color: white; background: #3F51B5; margin-top: 0;
                               font-size: 25px; padding: 10px 0;">Offer</h1>
                    <div style="padding: 20px; display: flex; flex-direction: column; font-size: 12px;">
                        <span><b>offer_id: </b><xsl:value-of select="offer/offer_id"/></span>
                        <span><b>equipment_type: </b><xsl:value-of select="offer/equipment_type"/></span>
                        <span><b>equipment_desc: </b><xsl:value-of select="offer/equipment_desc"/></span>
                        <span><b>equipment_location: </b><xsl:value-of select="offer/equipment_location"/></span>
                        <span><b>price ($): </b><xsl:value-of select="offer/price"/></span>
                        <span><b>expiry_after_days: </b><xsl:value-of select="offer/expiry_after_days"/></span>
                        <span><b>seller first_name: </b><xsl:value-of select="offer/seller/first_name"/></span>
                        <span><b>seller last_name: </b><xsl:value-of select="offer/seller/last_name"/></span>
                        <span><b>seller address: </b><xsl:value-of select="offer/seller/address"/></span>
                        <span><b>seller telephone: </b><xsl:value-of select="offer/seller/telephone"/></span>
                    </div>
                </div>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>