import json
import googletrans #import the googletrans module
from googletrans import Translator #import the Translator class from googletrans module
import codecs

def  getEndex(core, begIndex, sep):
  length = len(core)
  while ( begIndex < length and core[begIndex] != sep):
     begIndex= begIndex+1
  return begIndex


def  getHeadline(core, beg,end):
    return core[beg:end].strip()

if __name__ == "__main__":
 json_translate = Translator() #create an object
 filename="../src/data/Questions.json"
 i=0
 with open(filename, encoding="utf8") as f:
     data = json.load(f)
     questions= data["Questions"]
     while (i<len(questions)):
         qs=questions[i] #qs is a question
         core=qs["core"]
         clng = len(core)
         j = 0
         gathered=""
         translatedGathered=""
         translatedCore=""
         while (j < clng): #to translate the question's core without changing the images' names
             c = core[j]
             if ( c=='<' and (j+1)< clng):
                 if(core[j + 1] == '!'): # image spotted !
                     if len(gathered) >= 5 : 
                         gathered.strip()
                         translatedGathered= json_translate.translate(gathered).text
                         translatedCore=translatedCore+translatedGathered
                         gathered = "";  #empty
                         translatedGathered=""
                     endex = getEndex(core, j+2, '!')
                     img = getHeadline(core, j+2, endex)
                     translatedCore=translatedCore+"<!"+img+"!>"
                     j = endex+1    
             else:
                 gathered=gathered+c 
             j=j+1 
         translatedGathered=json_translate.translate(gathered).text
         translatedCore=translatedCore+translatedGathered
         core=translatedCore
         k=0
         while (k<len(qs['propositions'])): #to translate the propositions
             proposition = qs["propositions"][k]
             translatedProposition = json_translate.translate(proposition).text
             proposition=translatedProposition
             k=k+1
         note=qs["note"]
         translatedNote=json_translate.translate(note).text
         note=translatedNote
         i=i+1
     filename2="../src/data-english/Questions.json"
     with codecs.open(filename2, 'w', encoding='utf-8') as f2:
         json.dump(data, f2, indent=4,ensure_ascii=False)
    



