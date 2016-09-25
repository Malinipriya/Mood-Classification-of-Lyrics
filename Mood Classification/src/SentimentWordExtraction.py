'''
Created on 18-Nov-2015

@author: Malinipriya Goppannan Ravi
'''
import os
import re
from nltk.tag import pos_tag

output_file=open('angry25.txt','w')

#Read content from the input text file
file_name = input("Please enter the file name:")
dir_name = "D:/My docs/MS Course study/Project/Implementation/dataset/"
file = os.path.join(dir_name, file_name)
file_content = open(file).read()

#Split the file content into words and remove proper nouns
file_wordlist=[]
paragraphs = file_content.split("\n\n")
for para in paragraphs:
    para_content = para.split()
    for word in para_content:
        file_wordlist.append(re.sub(r'[^\w\s\'-]','',word))
    file_wordlist = [word.lower() for word in file_wordlist]
    tagged_sent = pos_tag(file_wordlist)
    for i,j in tagged_sent:
        if j=='NNP' or j=='NN' or j=='NNS' or j=='VB' or j=='VBD' or j=='VBG' or j=='VBN' or j=='VBP' or j=='VBZ' or j=='JJ' or j=='JJR' or j=='JJS' or j=='RB' or j=='RBR' or j=='RBS':
            output_file.write(i+" "+j)
            #print(i+" "+j)
            output_file.write('\n')
    file_wordlist=[]
    output_file.write('\n')
    
output_file.close()
print("Text extraction done!!")
