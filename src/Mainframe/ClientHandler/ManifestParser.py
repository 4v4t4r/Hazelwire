import xml.sax
import DatabaseHandler

modules = []
correctXML = True

class ManifestHandler(xml.sax.ContentHandler):

    isNameElement, isBasePathElement, isDeployScriptElement, startFlagsection = False,False,False,False

    def startElement(self, name, attrs):
        if name == "MODULE":
            modules.append({'name':'','numFlags':0,'basepath':'','deployscript':'', 'flagpoints':[]}) 
        elif name == "name":
            self.isNameElement = True
        elif name == "basepath":
            self.isBasePathElement = True
        elif name == "deployscript":
            self.isDeployScriptElement = True
        elif name == "flags":
            self.startFlagSection = True
        elif name == "flag" and self.startFlagSection:
            modules[-1]['flagpoints'].append(attrs.items()[0][1])

    def endElement(self, name):
        global correctXML
        if name == "name":
            self.isNameElement = False
        elif name == "basepath":
            self.isBasePathElement = False
        elif name == "deployscript":
            self.isDeployScriptElement = False
        elif name == "flags":
            self.startFlagSection = False
        elif name == "MODULE":
            modules[-1]['numFlags'] = len(modules[-1]['flagpoints'])
            if modules[-1]['numFlags'] == 0 or modules[-1]['name'] == '' or modules[-1]['basepath'] == '' or modules[-1]['deployscript'] == '':
                #incorrect XML
                correctXML = False
            

    def characters (self, ch):
        if self.isNameElement:
            modules[-1]['name'] += ch
        elif self.isBasePathElement:
            modules[-1]['basepath'] += ch
        elif self.isDeployScriptElement:
            modules[-1]['deployscript'] += ch


def parseManifest(manifest, db):
    parser = xml.sax.make_parser()
    parser.setContentHandler(ManifestHandler())
    parser.parse(open(manifest,"r"))
    if correctXML:
        db = DatabaseHandler.DatabaseHandler(db)
        db.addModuleInfo(modules)
        return True
    return False