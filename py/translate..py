import json
import googletrans #import the googletrans module
import os
from googletrans import Translator #import the Translator class from googletrans module

def getHeadlineDepth(core,begIndex):
 counter = 0
 length = len(core)
 while ((begIndex < length) and (core[begIndex] == '#')):
       counter = counter+1
       begIndex = begIndex+1
 return counter
    
def  getEndex(core, begIndex, sep):
  length = len(core)
  while ( begIndex < length and core[begIndex] != sep):
     begIndex= begIndex+1
  return begIndex


def  getHeadline(core, beg,end):
    return core[beg:end].strip()

if __name__ == "__main__": 
 json_translate = Translator() #create an object
 newDirectory="../src/data-english"
 os.mkdir(newDirectory)
 for folder in os.listdir('../src/data'):
      if folder.endswith('.json')==0: #Not a json file i.e a folder
         folderpath='../src/data/'+folder
         newfolderpath="../src/data-english/"+folder
         os.mkdir(newfolderpath) #Themex
         for file in os.listdir(folderpath):
            if file.endswith('.json') and file !="Overview.json": #just coursex.json
             filename=folderpath+"/"+file  #path to file
             with open(filename, encoding="utf8") as f:
                data = json.load(f)
                core=data["core"]
                title=data["title"]
                core = core.replace('\n', ' ')
                core = core.replace("  ", " ")
                core = core.strip()
                gathered = ""
                translatedCore=""
                translatedGathered=""
                translatedHeadline=""
                translatedList_el=""
                clng = len(core)
                i = 0
                translatedTitle= json_translate.translate(title).text
                while (i < clng):
                   c = core[i]
                   if c == '#':
                      # append gathered text first
                      if len(gathered) != 0 : #not empty
                         translatedGathered= json_translate.translate(gathered).text
                         translatedCore=translatedCore+translatedGathered
                         gathered = "";  #empty
                         translatedGathered=""
                      depth = getHeadlineDepth(core, i)
                      endex = getEndex(core, i+depth, '#')
                      headline = getHeadline(core, i+depth, endex)
                      #append new Headline
                      translatedHeadline=json_translate.translate(headline).text
                      translatedCore=translatedCore+translatedHeadline+'#'
                      i = endex
                   else:
                      if (c == '-' and (i + 1)< clng):
                         if(core[i + 1] == '>'): #element list spotted !
                            endex = getEndex(core, i+2, '#')
                            list_el = getHeadline(core, i+2, endex)
                            #append new Headline
                            translatedList_el = json_translate.translate(list_el)
                            translatedCore=translatedCore+"->"+translatedList_el.text+"#"
                            i = endex 
                      else:
                         if ( c=='<' and (i+1)< clng):
                            if(core[i + 1] == '!'): # image-?list spotted !
                               endex = getEndex(core, i+1, '!')
                               list = getHeadline(core, i+2, endex)
                               list=list.strip()
                               #list.replace(" I","i")
                               translatedCore=translatedCore+"<!"+list+"!>"
                               i = endex
                            else:
                               gathered= gathered + c
                               i = i+1
                filename2=newfolderpath+"/"+file
                data["core"] = translatedCore
                data["title"] = translatedTitle
                with open(filename2, 'w', encoding='utf-8') as f2:
                   json.dump(data, f2, indent=4)
                   f2.close()
                f.close()
                              


