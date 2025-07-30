
How to edit this: test-2025
* Option-1 (tested and worked ok): PyCharm supports editing of these .md files. Then you need to push to the main branch or make a pull request.
* Option-2 (tested ok): Edit and commit it directly on github. Then any local repo copies will need to be "updated".

# cp3407-House cleaning booking app
* This a project assignment template for CP3407. The following is the list of items, which are required to be completed.


## How to submit [You may delete this before submitting]

#### 2. Add your instructor (JCU-Australia: jc138691@gmail.com Dmitry Konovalov) as a team member to view your project on github
#### 1. Submit link to your repository
#### 2. Download your github repository as a zip file and submit via LearnJCU.

## Team

It is recommended to complete this assignment in a group of 2-4 students.
1. Yuanuri Gao
2. Hao Zhang
3. Chunhan Liu
4. ZhiXing Li


# Project planning BEFORE iteration-1
### Main Gold of the Application
   #### We plan to create a mobile phone-based house cleaning appointment software, which has two functions: user-oriented and employee-oriented.

####    For users, users can log in to store their house information locally and use the software to make an appointment with a nearby registered cleaning company for door-to-door service.

####    For employees (registered cleaning company staff), the function is to store employees' personal information and provide services to nearby users who have made an appointment through the software.

####    While completing the above functions, the software can also become a platform for direct communication between users and staff.

### All User Stories

1.Title: Book Cleaning Service, Priority: High, Estimate: 5 day
2.Title: View Cleaner Profiles, Priority: Medium, Estimate: 3 day
3.Title: Payment via Card/PayPal, Priority: High, Estimate: 6 day
4.Title: Cleaner Availability Management, Priority: Medium, Estimate: 4 day
5.Title: Receive Booking Notifications, Priority: Low, Estimate: 2 day
6.Title: In-App Chat with Cleaner, Priority: Medium, Estimate: 4 day
7.Title: Manage Bookings and Cancellations, Priority: High, Estimate: 6 day


   Total: 30 days


### Project Timeline

  | No.|   Phase	              |Duration                    |Weeks         |
  |----|:----------------------:|:--------------------------:|:------------:|
  | 1. | Iteration 1            |   4 weeks                  |   Week 3 – 6   |
  | 2. | Iteration 2            |   3 weeks                  |   Week 7 – 10  |
  

## Iteration 1 [week 3 - week 6 ], start date: 2025/6/6 -->  

1. [Book Cleaning Service](./user_stories/user_story_01_Book _Cleaning_Service.md), priority 10(Heigh), 6 days
        The app will allow users to select a cleaning service, including the type of service, time and cleaner, and submit a booking request.

        Tasks
        1. Yuanuri Gao, Hao Zhang
            1. Design booking screen UI
            2. Implement time and cleaner selection
        2. Chunhan Liu
            1. Backend integration for booking submission
            2. Save booking in database and confirm status
        
        

2. [View Cleaner Profiles](./user_stories/user_story_02_View_Cleaner_Profiles.md), priority 7(Medium), 3 days
        Users can browse cleaner profiles to compare qualifications, ratings, and availability.

        Tasks
        1. Chunhan Liu
            1. UI layout for profile cards
            2. Connect to API and fetch cleaner data
        1. ZhiXing Li
            3. Detail view for each cleaner

3. [Payment via Card/PayPal](./user_stories/user_story_03_Payment via Card/PayPal.md), priority 10(Heigh), 6 days
        Users can securely complete payments via credit/debit card or PayPal.

        Tasks
        1. Yuanuri Gao
            1. Payment method selection screen
            2. Integration of payment SDKs
            3. Handle payment result callbacks
        2. Hao Zhang
            1. Store payment confirmation

4. [Cleaner Availability Management](./user_stories/user_story_04_Cleaner Availability Management.md), priority 7(Medium), 4 days
        Cleaners can set and manage their available time slots in the app.

        Tasks
        1. Chunhan Liu
            1. UI for weekly schedule selection
            2. Save availability to backend
        2. ZhiXing Li
            1. Handle payment result callbacks
            2. Sync availability with booking engine

5. [Receive_Notifications](.user_stories/user_story_05_Receive_Notifications.md), priority 5(Low), 2 days
        Users receive push notifications about upcoming bookings and reminders before the scheduled service time.

        Tasks
        1. Yuanuri Gao, Hao Zhang
            1. Implement push notification logic
        3. Chunhan Liu, ZhiXing Li
            2. Create triggers for booking confirmation and reminders

Total: 21 days

4.Title: Cleaner Availability Management, Priority: Medium, Estimate: 4 day
5.Title: Receive Booking Notifications, Priority: Low, Estimate: 2 day
6.Title: In-App Chat with Cleaner, Priority: Medium, Estimate: 4 day
7.Title: Manage Bookings and Cancellations, Priority: High, Estimate: 6 day
## Iteration 2 [week 6 - week 9 ], add your start and end dates

### Main Goal for Iteration-2

#### Deliver a functional core gym access system that enables:

* The usability and stability of the app are ensured to prevent any special circumstances from causing it to be unusable.

* Connect the app to the notification function of the device

* The function for modifying and canceling orders

* The personalized minor functions for both parties ensure smooth communication between them.

### User Story

| ID    | Title                                 | Priority  | Est. Days | Notes                                |
| --    | -------------------------------       | --------  | --------- | ------------------------------------ |
| 4     | Cleaner Availability Management       | Medium    | 4 day     | Design a simple interface to make the app easy to use and enjoyable.|
| 5     | Receive Booking Notifications         | Low       | 2 day     | It enables the app to send reminders to the device, with options for ringtone alerts and displaying information at the top of the phone. |
| 6     | In-App Chat with Cleaner              | Medium    | 4 days    | Enable employers and cleaners to communicate directly within the app  |
| 7     | Manage Bookings and Cancellations     | High      | 6 days    | Enable employers to cancel reservations through the app, and send reminders and confirmations to the cleaners. |


# Actual iterations
1. [Iteration-1](./iteration_1.md)
2. [Iteration-2](./iteration_2.md)




