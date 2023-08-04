import xml.etree.ElementTree as ET
import pandas as pd
import numpy as np

def trav_tree(code, root):
    if root.text != None and root.text != "\n":
        code += root.text + "\n" 
    for child in root:
        code = trav_tree(code, child)
    if root.tail != None and root.text != "\n":
        code += root.tail + "\n"
    return code


cwe_collect = []
 
tree = ET.parse("cwe.xml")
# 根节点
root = tree.getroot()
# 标签名
Weaknesses_root = None
for child in root:
    if "Weaknesses" in child.tag:
        Weaknesses_root = child
        break
flag = 0
for weakness in Weaknesses_root:
    cwe_id = weakness.attrib['ID']
    cwe_name = weakness.attrib['Name']
    example_code = []
    for attr in weakness:
        # parse for the cwe msg
        # parse for the example java code
        if "Demonstrative_Examples" in attr.tag:
            for demonstrative_example in attr:
                for ele in demonstrative_example:
                    if "Example_Code" in ele.tag:
                        #print(ele.attrib)
                        if ('Nature' in ele.attrib and ele.attrib['Nature'] == "Bad") and ('Language' in ele.attrib and  ele.attrib['Language'] == 'Java'):
                            for c in ele:
                                code = ""
                                code = trav_tree(code, c)
                                example_code.append(code)
    for i in range(0, len(example_code)):
        cwe_collect.append([cwe_id, cwe_name, example_code[i]])

#df = pd.DataFrame(np.array(cwe_collect), columns=["cwe-id", "cwe-name", "example-code"])
#df.to_csv("cwe_java_official.csv")
path = "./CWE-Official/"
index_map = []
for i in range(0, len(cwe_collect)):
    index_map.append([cwe_collect[i][0], i + 1])
    file_name = "./CWE-Official/{0}.java".format(i + 1)
    fp = open(file_name, "w")
    fp.write(cwe_collect[i][2])
    fp.close()

df = pd.DataFrame(np.array(index_map), columns=['cwe-type', 'source-code'])
df.to_csv("cwe_java_official.csv")
    