'''
Created on 19-Nov-2015

@author: dell
'''
'''
Created on 15-Nov-2015

@author: Malinipriya Goppannan Ravi
'''
output_file=open('pos_angry6.txt','w')

#Read content from the input text file
content=[]
file = input("Please enter the file name:")
file_content = open(file).readlines()
for line in file_content:
    content.append(line.rstrip("\n"))
for line in content:
    temp=line.split()
    #print(temp[0]+" "+temp[1])
    if(len(temp)>0):
        if temp[1]=='NNP' or temp[1]=='NN' or temp[1]=='NNS':
            output_file.write(temp[0]+" "+"n")
            output_file.write("\n")
        elif temp[1]=='VBP' or temp[1]=='VB' or temp[1]=='VBZ' or temp[1]=='VBN' or temp[1]=='VBG':
            output_file.write(temp[0]+" "+"v")
            output_file.write("\n")
        elif temp[1]=='JJ' or temp[1]=='JJR' or temp[1]=='JJS':
            output_file.write(temp[0]+" "+"a")
            output_file.write("\n")
        elif temp[1]=='RB' or temp[1]=='RBR' or temp[1]=='RBS':
            output_file.write(temp[0]+" "+"r")
            output_file.write("\n")
    else:
        output_file.write("\n")
output_file.close()
#print(content)
