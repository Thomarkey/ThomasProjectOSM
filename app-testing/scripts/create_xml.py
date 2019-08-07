#!/usr/bin/env python
#
# This script parses Cucumber reports in JSON format, which where generated using Cucable.
# It discoveres the device name in the report, and then sets the name of the feature
# in the report to the name of the device.

import argparse
import xml.etree.ElementTree as ET


def main():
    #Parsing of the arguments
    args = parse_args()
    devices = args.devices.split(',')
    threads = args.thread_count

    #Creating the suite
    root = ET.Element("suite", name="Parallel-Test-Suite", parallel="tests")
    root.set("thread-count", threads)

    for device in devices:
        #Adding tests to the suite
        doc = ET.SubElement(root, "test", name="Test-" + device)
        ET.SubElement(doc, "parameter", name="device", value=device)

        #Defining the test executor
        classes = ET.SubElement(doc, "classes")
        ET.SubElement(classes, "class", name="be.refleqt.projectname.tests.TestExecutor")

    #Parsing of the XML
    tree = ET.ElementTree(root)

    #Creating/Rewriting the file
    with open('../testSuite.xml', 'wb') as f:
        #Adding the XML version + Doctype to the XML
        f.write('<?xml version="1.0" encoding="UTF-8" ?><!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">'.encode('utf8'))
        #Adding the XML values
        tree.write(f, 'utf-8')



def parse_args():
    parser = argparse.ArgumentParser()

    parser.add_argument("-d",
                        "--devices",
                        required=True,
                        help="List of devices seperated by a ,"
                             "example: IPHONE_6,IPHONE_7,...",
                        type=str
                        )

    parser.add_argument("-tc",
                        "--thread-count",
                        required=False,
                        help="Amounts of thread to use, default value is 6",
                        default="12",
                        type=str
                        )

    return parser.parse_args()


if __name__ == "__main__":
    main()