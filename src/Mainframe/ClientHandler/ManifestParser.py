import xml.sax
import DatabaseHandler

modules = []

class ManifestHandler(xml.sax.ContentHandler):

    isNameElement, isNumFlagsElement, isBasePathElement, isDeployScriptElement, startFlagsection = False,False,False,False,False

    def startElement(self, name, attrs):
        if name == "MODULE":
            modules.append({'name':'','numFlags':0,'basepath':'','deployscript':'', 'flagpoints':[]}) 
    	elif name == "name":
	        self.isNameElement = True
        elif name == "numFlags":
            self.isNumFlagsElement = True
        elif name == "basepath":
            self.isBasePathElement = True
        elif name == "deployscript":
            self.isDeployScriptElement = True
        elif name == "flags":
            self.startFlagSection = True
        elif name == "flag" and self.startFlagSection:
            modules[-1]['flagpoints'].append(attrs.items()[0][1])
        
    def endElement(self, name):
        if name == "name":
	        self.isNameElement = False
        elif name == "numFlags":
            self.isNumFlagsElement = False
        elif name == "basepath":
            self.isBasePathElement = False
        elif name == "deployscript":
            self.isDeployScriptElement = False
        elif name == "flags":
            self.startFlagSection = False
    
    def characters (self, ch):
        if self.isNameElement:
            modules[-1]['name'] += ch
        elif self.isNumFlagsElement:
            modules[-1]['numFlags'] += int(ch)
        elif self.isBasePathElement:
            modules[-1]['basepath'] += ch
        elif self.isDeployScriptElement:
            modules[-1]['deployscript'] += ch


def parseManifest(manifest):
    parser = xml.sax.make_parser()
    parser.setContentHandler(ManifestHandler())
    parser.parse(open("../example_MANIFEST.xml","r"))
    DatabaseHandler.addModuleInfo(modules)

if __name__ == "__main__":
    parseManifest("example_MANIFEST.xml")