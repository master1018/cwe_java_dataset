import os
import json
import random

GPT_DATASET = "./gpt_dataset/"

def split_gpt_dataset(split_rate):
    fp = open("./gpt_dataset/index.json", "r")
    content = fp.read()
    fp.close()
    index = json.loads(content)
    
    os.system("mkdir ./rule_set/")
    os.system("mkdir ./input_set/")

    for cwe_map in index:
        num_of_file = cwe_map['end'] - cwe_map['start'] + 1
        cwe_file = []
        for i in range(cwe_map['start'], cwe_map['end'] + 1):
            cwe_file.append(GPT_DATASET + "{0}.java".format(i))

        num_of_rule = (int)(num_of_file * split_rate)
        num_of_input = num_of_file - num_of_rule

        rule_file = []
        input_file = []

        for i in range(0, num_of_input):
            chose = random.randrange(len(cwe_file))
            input_file.append(cwe_file[chose])
            cwe_file.pop(chose)

        rule_file = cwe_file

        for i in range(0, len(rule_file)):
            os.system("cp " + rule_file[i] + " ./rule_set/")
        for i in range(0, len(input_file)):
            os.system("cp " + input_file[i] + " ./input_set/")
        

split_gpt_dataset(0.6)


