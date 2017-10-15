/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */
package org.apache.poi.xdgf.extractor;

import java.io.IOException;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xdgf.usermodel.XDGFPage;
import org.apache.poi.xdgf.usermodel.XmlVisioDocument;
import org.apache.poi.xdgf.usermodel.shape.ShapeTextVisitor;

/**
 * Helper class to extract text from an OOXML Visio File
 */
public class XDGFVisioExtractor extends POIXMLTextExtractor {

    protected final XmlVisioDocument document;
    
    public XDGFVisioExtractor(XmlVisioDocument document) {
        super(document);
        this.document = document;
    }

    public XDGFVisioExtractor(OPCPackage openPackage) throws IOException {
        this(new XmlVisioDocument(openPackage));
    }

    public String getText() {
        ShapeTextVisitor visitor = new ShapeTextVisitor();
        
        for (XDGFPage page: document.getPages()) {
            page.getContent().visitShapes(visitor);
        }
        
        return visitor.getText();
    }
    
    public static void main(String [] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Use:");
            System.err.println("  XDGFVisioExtractor <filename.vsdx>");
            System.exit(1);
        }
        POIXMLTextExtractor extractor =
                new XDGFVisioExtractor(POIXMLDocument.openPackage(
                        args[0]
                ));
        System.out.println(extractor.getText());
        extractor.close();
    }
}
