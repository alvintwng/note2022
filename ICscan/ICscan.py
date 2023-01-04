import asyncio
import threading
import requests
import concurrent.futures
import pandas as pd
import os
import io
import shutil
from google.cloud import vision
import imghdr
import warnings
warnings.simplefilter(action='ignore', category=FutureWarning)
import xlsxwriter


def PathAnalyzer(PATH):

    def imgtotext(img):
        os.environ['GOOGLE_APPLICATION_CREDENTIALS'] = r'vocal-chiller-351412-df1a2005b608.json'
        client = vision.ImageAnnotatorClient()

        with io.open(img, 'rb') as image_file:
            content = image_file.read()

        image = vision.Image(content=content)

        response = client.text_detection(image=image)

        texts = response.text_annotations

        df = pd.DataFrame(columns=['locale', 'description'])

        for text in texts:
            df = df.append(
                dict(
                    Locale=text.locale,
                    description=text.description
                ),
                ignore_index=True
            )

        with open( "test.txt", 'a') as f:
            print("I am writing")
            f.write('\n')
            f.write('\n')
            f.write(img)
            f.write('\n')
            for line in df['description'][0].replace("\u4e8b", "").replace("\n", " ").replace("Immigration Regulations", " ").replace('YOU ARE TO SURRENDER THIS CARD WHEN IT IS CANCELLED OR HAS EXPIRED, OR WHEN A NEW CARD IS ISSUED TO YOU', "").replace(" THIS PASS WILL NOT BE VALID AND SHALL BE SURRENDERED FOR CANCELLATION WITHIN 7 DAYS IF HOLDER CEASES TO BE A STUDENT WITH THE SCHOOL FOR WHICH THIS PASS WAS ISSUED", " ").replace("Republic of Singapore", "").replace("REPUBLIC OF SINGAPORE", "").replace("SINGAPORE", "").replace("MAJULAH", "").replace("SINGAPURA", "").replace("MINISTRY", "").replace("OF", "").replace("MANPOWER", "").replace("MANPOWE", "").replace("MANPOW", "").replace("*", "").replace("HOME", "").replace("AFFAIRS", "").replace("Employment of Foreign Manpower Act (Chapter 91A)", "").replace("FIN ", "\nFIN\n").replace("Name ", "\nName\n").replace("Employer ", "\nEmployer\n").replace("Nationality ", "\nNationality\n").replace("Sex ", "\nSex\n").replace("Date of Birth ", "\nDate of Birth\n").replace("Date of Application ", "\nDate of Application\n").replace("Date of Issue ", "\nDate of Issue\n").replace("Date of Expiry ", "\nDate of Expiry\n").replace("Occupation ", "\nOccupation\n").replace("S Pass No ", "\nS Pass No\n").replace("Work ", "\nWork\n").replace("Sector: ", "\nSector\n").replace("Sex\n\nDate of Birth", "Gender and Date of Birth").replace("Date of Birth\n\nSex", "Date of Birth and Gender").replace("WORK PERMIT", "\nWORK PERMIT\n").replace("VISIT PASS", "\nVISIT PASS\n").replace("DEPENDENT'S PASS", "\nDEPENDENT'S PASS\n").replace("MULTIPLE JOURNEY VISA ISSUED", "").replace("STUDENT'S PASS", "\nSTUDENT'S PASS\n").replace("VISIT PASS", "\nVISIT PASS\n").replace("Download", "").replace("SG", "").replace("to", "").replace("check", "").replace("status", "").replace("MULTIPLE JOURNEY VISA ISSUED", "").replace("CARD", "").replace("CANCELLED", "").replace("FIN\n\nDate of Issue\n\nDate of Expiry", "FIN and Date Of Issue and Date Of Expiry").replace("Date of Issue\n\nDate of Expiry", "Date of Issue and Date of Expiry").replace("Date of Birth and Gender\n\nNationality", "Nationality and Date of Birth and Gender"):
                f.write(line)
# TESTING SCRIPTS

        # async def getFIN(line, myfile):
        #     if line.startswith("FIN"):
        #         if line.startswith("FIN and Date Of Issue and Date Of Expiry"):
        #             list = next(myfile).split(" ")
        #             print("FIN")
        #             print('\n')
        #             print(list[0])
        #         else:
        #             print(line)
        #             print(next(myfile).split(" ")[0])
        #             print('\n')

        # async def getName(line, myfile):
        #     if line.startswith("Name"):
        #         print(line)
        #         print(next(myfile))

        # async def getNationality(line, myfile):
        #     if line.startswith("Nationality and Date of Birth and Gender"):
        #         list = next(myfile).split(" ")
        #         print("Nationality")
        #         print('\n')
        #         print(list[0])
        #     elif line.startswith("Nationality"):
        #         list = next(myfile).split(" ")
        #         print(line)
        #         print(list[0])
        #         print('\n')

        # async def getDOB(line, myfile):
        #     if line.startswith("Nationality and Date of Birth and Gender"):
        #         list = next(myfile).split(" ")
        #         print("Date of Birth")
        #         print(list[1])
        #         print("\n")
        #     elif line.startswith("Date of Birth and Gender"):
        #         list = next(myfile).split(" ")
        #         if list[1] == 'M' or list[1] == 'F':
        #             print("Date of Birth")
        #             print(list[0])
        #             print('\n')

        #         else:
        #             print("Date of Birth")
        #             print(list[1])
        #             print('\n')

        #     if line.startswith("Gender and Date of Birth"):
        #         list = next(myfile).split(" ")
        #         if list[0] == 'M' or list[0] == 'F':
        #             print("Date of Birth")
        #             print(list[1])
        #             print('\n')
        #         else:
        #             print("Date of Birth")
        #             print(list[0])
        #             print('\n')

        # async def getGender(line, myfile):
        #     if line.startswith("Sex"):
        #         print("Gender")
        #         print(next(myfile))
        #     if line.startswith("Nationality and Date of Birth and Gender"):
        #         list = next(myfile).split(" ")
        #         print("Gender")
        #         print(list)
        #     if line.startswith("Date of Birth and Gender"):
        #         list = next(myfile).split(" ")
        #         # if list[1] == 'M' or list[1] == 'F':
        #         #     print("Gender")
        #         #     print(list[1])
        #         # else:
        #         print("Gender")
        #         print(list)
        #     if line.startswith("Gender and Date of Birth"):
        #         list = next(myfile).split(" ")
        #         if list[0] == 'M' or list[0] == 'F':
        #             print("Gender")
        #             print(list[0])
        #         else:
        #             print("Gender")
        #             print(list)

        # async def getEmployer(line, myfile):
        #     if line.startswith("Employer"):
        #         print(line)
        #         print(next(myfile))

        # async def getDOA(line, myfile):
        #     if line.startswith("Date of Application"):
        #         print(line)
        #         print(next(myfile).split(" ")[0])
        #         print('\n')

        # async def getSector(line, myfile):
        #     if line.startswith("Sector"):
        #         print(line)
        #         print(next(myfile).split(" ")[0])
        #         print('\n')

        # async def getDOI(line, myfile):
        #     if line.startswith("Date of Issue"):
        #         if line.startswith("Date of Issue and Date of Expiry"):
        #             list = next(myfile).split(" ")
        #             print("Date of Issue")
        #             print('\n')
        #             print(list[0])
        #             print('\n')
        #         else:
        #             print(line)
        #             print(next(myfile).split(" ")[0])
        #             print('\n')

        # async def getDOE(line, myfile):
        #     if line.startswith("Date of Issue and Date of Expiry"):
        #         list = next(myfile).split(" ")
        #         print("Date of Expiry")
        #         print('\n')
        #         print(list)
        #         print('\n')
        #     if line.startswith("Date of Expiry"):
        #         print(line)
        #         print(next(myfile).split(" ")[0])
        #         print('\n')

        # async def thefunc():
        #     await asyncio.gather(getFIN(line, myfile),getName(line, myfile),getNationality(line, myfile),getDOB(line, myfile),getGender(line, myfile),getEmployer(line, myfile),getDOA(line, myfile),getSector(line, myfile),getDOI(line, myfile),getDOE(line, myfile))

        workbook = xlsxwriter.Workbook('hello.xlsx')
 
# The workbook object is then used to add new
# worksheet via the add_worksheet() method.
        worksheet = workbook.add_worksheet() 
        # id_type =""
        # id_number =""
        # name =""
        # company= ""
        # gender =""
        # address = ""
        # nationality =""
        # date_of_issue =""
        # date_of_expiry =""
        # date_of_application =""
        counter = 0
        row =1
        column = 0

        with open('test.txt', 'rt') as myfile:

            for line in myfile:
                if line.startswith("Test_Images/IDImages/IDImages/"):
                    counter+=1
                    if counter%2 !=0:
                        row+=1
                    
                elif line.startswith("FIN"):
                    if line.startswith("FIN and Date Of Issue and Date Of Expiry"):
                        list = next(myfile).split(" ")
                        # print("FIN")
                        # print('\n')
                        worksheet.write(row,column+1,list[0])   
                        worksheet.write(row,column+6,list[1])
                        worksheet.write(row,column+7,list[2])
                   
                    
                        # print('\n')
                        # print("Date of Issue")
                        # print(list[1])
                        # print("\n")
                        # print("Date of Expiry")
                        # print(list[2])
                    else:
                        # print(line)
                         worksheet.write(row,column+1,next(myfile).split(" ")[0])
                        
                        
                elif(line.__contains__("WORK PERMIT")):
                    worksheet.write(row,column+5,"WORK PASS")
                
                    
                # elif(line.__contains__("S PASS")):
                #     worksheet.write(row,column+5,"S PASS")
                #    +=1
                
                elif(line.__contains__("STUDENT'S PASS")):
                    worksheet.write(row,column+5,"STUDENT'S PASS")
                   
                
                elif(line.__contains__("VISIT PASS")):
                    worksheet.write(row,column+5,"VISIT PASS")
              
                    
                elif(line.__contains__("EMPLOYMENT PASS")):
                    worksheet.write(row,column+5,"EMPLOYMENT PASS")
                
                
                elif(line.__contains__("DEPENDANT'S PASS")):
                    worksheet.write(row,column+5,"DEPENDANT'S PASS")
                    
                      
                elif line.startswith("Name"):
                    # print(line)
                    line = next(myfile)
                    worksheet.write(row,column,line)
                       
        

                elif line.startswith("Nationality"):
                    if line.startswith("Nationality and Date of Birth and Gender"):
                        list = next(myfile).split(" ")
                        # print("Nationality")
                        # print('\n')
                        worksheet.write(row,column+2,list[0])
                    
                        # print('\n')
                        # print("Date of Birth")
                        worksheet.write(row,column+3,list[1])

                        # print("\n")
                        # print("Gender")
                        worksheet.write(row,column+4,list[2])

                    else:
                        # print(line)
                        worksheet.write(row,column+2,next(myfile).split(" ")[0])

                        # print(next(myfile).split(" ")[0])
                        # print('\n')
                        
                elif line.startswith("Date of Birth"):
                        if line.startswith("Date of Birth and Gender"):
                                list = next(myfile).split(" ")
                                if list[1] == 'M' or list[1] == 'F':
                                        # print("Date of Birth")
                                        worksheet.write(row,column+3,list[0])
                                  
                                        # print('\n')
                                        # print("Gender")
                                        worksheet.write(row,column+4,list[1])
                                   
                                else:
                                        # print("Date of Birth")
                                        worksheet.write(row,column+3,list[1])
                                   
                                        # print('\n')
                                        # print("Gender")
                                        worksheet.write(row,column+4,list[0])
                                   
                        else:
                            worksheet.write(row,column+3,next(myfile))
                       
                elif line.startswith("Gender and Date of Birth"):
                        list = next(myfile).split(" ")
                        if list[0] == 'M' or list[0] == 'F':
                                # print("Date of Birth")
                                worksheet.write(row,column+3,list[1])
                            
                                # print(list[1])
                                # print('\n')
                                # print("Gender")
                                worksheet.write(row,column+4,list[0])
                           
                        else:
                                # print("Date of Birth")
                                worksheet.write(row,column+3,list[0])
                            
                                # print(list[0])
                                # print('\n')
                                # print("Gender")
                                worksheet.write(row,column+4,list[1])
                          
                       
                               
                # if line.startswith("Date of Birth "):
                #          worksheet.write(row,column+3,next(myfile))
                #          row+=1
                elif line.startswith("Sex"):
                    #print(line)
                    worksheet.write(row,column+4,next(myfile))
               
                # if line.startswith("Employer"):
                #     print(line)
                #     print(next(myfile))
                elif line.startswith("Date of Application"):
                #     print(line)
                    worksheet.write(row,column+8,(next(myfile)))
                #     print('\n')
                elif line.startswith("Date of Issue"):
                    if line.startswith("Date of Issue and Date of Expiry"):
                        list = next(myfile).split(" ")
                        # print("Date of Issue")
                        # print('\n')
                        # print(list[0])
                        # print('\n')
                        # print("Date of Expiry")
                        # print(list[1])
                        # print("\n")
                        worksheet.write(row,column+6,list[0])
                        worksheet.write(row,column+7,list[1])
                    else:
                        # print(line)
                        line = next(myfile).split(" ")[0]
                        #print(line)
                        worksheet.write(row,column+6,(line))
                        # print('\n')
                elif line.startswith("Date of Expiry"):
                    # print(line)
                    line = next(myfile).split(" ")[0]
                    print(line)
                    worksheet.write(row,column+7,(line))
                    # print('\n')
                # if line.startswith("Sector"):
                #     print(line)
                #     print(next(myfile).split(" ")[0])
                #     print('\n')
        workbook.close()

                # read_file = pd.read_csv(
                #     r'C:/Users/Yuvaraj/Desktop/OCR/VisionAPIDemo/test.txt')
                # read_file.to_csv(
                #     r'C:/Users/Yuvaraj/Desktop/OCR/VisionAPIDemo/test.csv', index=0)
                
    with concurrent.futures.ThreadPoolExecutor() as executor:
        for filename in os.listdir(PATH):
            f = os.path.join(PATH, filename)
            if(os.path.isfile(f)):
                if(imghdr.what(f) == 'jpeg' or imghdr.what(f) == 'png' ):
                    (imgtotext(f))
                # else:
                #     shutil.move(f, 'ERROR')
            elif(os.path.isdir(f) ):
                PathAnalyzer(f)
    # imgtotext(PATH)

PathAnalyzer("Test_Images")

# def fn():       # 1.Get file names from directory
#     file_list=os.listdir("Test_Images/IDImages/IDImages")
#     return file_list
    
# lister = fn()




# excecutor = concurrent.futures.ThreadPoolExecutor(max_workers=200)
# excecutor.map(PathAnalyzer,lister)