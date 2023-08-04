import xml.etree.ElementTree as ET
 
tree = ET.parse("cwe.xml")
# 根节点
root = tree.getroot()
# 标签名
Weaknesses_root = None
for child in root:
    if "Weaknesses" in child.tag:
        Weaknesses_root = child
        break
for weakness in Weaknesses_root:
    print("------------------")
    print(weakness.attrib)
    for ele in weakness:
        print(ele)
    break
