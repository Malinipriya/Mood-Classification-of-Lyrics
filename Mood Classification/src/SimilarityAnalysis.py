'''
Created on 19-Nov-2015

@author: Malinipriya Goppannan Ravi
'''
from nltk.corpus import wordnet as wn
import os
#from itertools import *
#output_file=open('D:/eclipse/workspace/KMeansClustering/similarity_results/angry6.txt','w')

#file_name = input("Please enter the file name:")
#dir_name = "D:/eclipse/workspace/KMeansClustering/clustering_results"
#file = os.path.join(dir_name, file_name)
#file_content = open(file).readlines()

angry=[]
for synset in wn.synsets('anger'):
    for lemma in synset.lemmas():
        angry.append(lemma.name())
    for s in synset.closure(lambda s:s.hyponyms()): 
        for w in s.lemma_names():
            angry.append(w)
    for s in synset.closure(lambda s:s.hypernyms()): 
        for w in s.lemma_names():
            angry.append(w)
angry=set(angry)
#print(angry)

happy=[]
for synset in wn.synsets('happy'):
    for lemma in synset.lemmas():
        happy.append(lemma.name())
    for s in synset.closure(lambda s:s.hyponyms()): 
        for w in s.lemma_names():
            happy.append(w)
    for s in synset.closure(lambda s:s.hypernyms()): 
        for w in s.lemma_names():
            happy.append(w)
happy=set(happy)
#print(happy)

sad=[]
for synset in wn.synsets('sad'):
    for lemma in synset.lemmas():
        sad.append(lemma.name())
    for s in synset.closure(lambda s:s.hyponyms()): 
        for w in s.lemma_names():
            sad.append(w)
    for s in synset.closure(lambda s:s.hypernyms()): 
        for w in s.lemma_names():
            sad.append(w)
sad=set(sad)
#print(sad)

love=[]
for synset in wn.synsets('love'):
    for lemma in synset.lemmas():
        love.append(lemma.name())
    for s in synset.closure(lambda s:s.hyponyms()): 
        for w in s.lemma_names():
            love.append(w)
    for s in synset.closure(lambda s:s.hypernyms()): 
        for w in s.lemma_names():
            love.append(w)
love=set(love)
#print(love)

p1="D:/eclipse/workspace/KMeansClustering/clustering_results"
p2='D:/eclipse/workspace/KMeansClustering/similarity_results'
for i in os.listdir(p1):
    print(i)
    file = os.path.join(p1, i)
    file_content = open(file).readlines()
    output_file = os.path.join(p2, i)
    output_file=open(output_file,'w')
    para_synsets=[]
    i=0
    for line in file_content:
        print(line)
        if line not in ['\n', '\r\n']:
            #print("inside if")
            line=line.split()
            wordpos=line[0]
            word_and_pos=wordpos.split('#')
            list2=wn.synsets(word_and_pos[0])
            for synset in list2:
                for lemma in synset.lemmas():
                    para_synsets.append(lemma.name())
                for s in synset.closure(lambda s:s.hyponyms()): 
                    for w in s.lemma_names():
                        para_synsets.append(w)
                for s in synset.closure(lambda s:s.hypernyms()): 
                    for w in s.lemma_names():
                        para_synsets.append(w)
            #print(para_synsets)
        else:
            para_set=set(para_synsets)
            i=i+1
            output_file.write("\n")
            output_file.write("Results for cluster "+str(i)+"\n")
            output_file.write("\n")
            overlapping_words=set.union(angry,happy)
            overlapping_words=set.union(overlapping_words,love)
            overlapping_words=set.union(overlapping_words,sad)
            overlapping_words=set.intersection(para_set,overlapping_words)
            output_file.write(str(overlapping_words))
            output_file.write("\n")
            para_synsets=[]
            list2=[]
            output_file.write("Length of overlapping words: "+str(len(overlapping_words))+"\n")
    output_file.close()
print("output written")
