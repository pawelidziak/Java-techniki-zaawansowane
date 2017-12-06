<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/">
        <html>
            <body style="font-family: Verdana, sans-serif;">
                <h1 style="text-align: center;">Offer</h1>
                <table border="1">
                    <tr bgcolor="#4CAF50" style="font-size: 12px;">
                        <th>offer_id</th>
                        <th>equipment_type</th>
                        <th>equipment_desc</th>
                        <th>equipment_location</th>
                        <th>price ($)</th>
                        <th>expiry_after_days</th>
                        <th>seller first_name</th>
                        <th>seller last_name</th>
                        <th>seller address</th>
                        <th>seller telephone</th>
                    </tr>
                    <tr style="font-size: 11px;">
                        <td><xsl:value-of select="offer/offer_id"/></td>
                        <td><xsl:value-of select="offer/equipment_type"/></td>
                        <td><xsl:value-of select="offer/equipment_desc"/></td>
                        <td><xsl:value-of select="offer/equipment_location"/></td>
                        <td><xsl:value-of select="offer/price"/></td>
                        <td><xsl:value-of select="offer/expiry_after_days"/></td>
                        <td><xsl:value-of select="offer/seller/first_name"/></td>
                        <td><xsl:value-of select="offer/seller/last_name"/></td>
                        <td><xsl:value-of select="offer/seller/address"/></td>
                        <td><xsl:value-of select="offer/seller/telephone"/></td>
                    </tr>
                </table>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>