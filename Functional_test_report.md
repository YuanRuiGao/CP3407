# Project Background
### The House Cleaning App (hereinafter referred to as the "App") is dedicated to providing users with convenient home cleaning service booking, service execution, and payment capabilities. This report summarizes the issues encountered during actual use and explains the processes and causes.
# I.Test the login function
## First Test Process:
#### 1. Launch the app and enter the login page.
#### 2. Enter your phone number/email address in the "Account" input box and log in.
#### 3. Log out.
#### 4. Log back in.
### Expected Result:
#### Account information should be preserved.
### Result Description:
#### User feedback: After logging in, all user accounts store not only their own information but also other users' information.
### Impact:
#### User experience is impacted and user information is leaked.
### Cause Analysis:
#### Possibly due to an error in the database sharing (perhaps due to incorrect account permissions), allowing each user account to access all database information.
### Possible Actions:
#### 1. When using SQLiteDatabase, check the permission mode of the openOrCreateDatabase method.
#### 2. When using Room or Firebase, check the user data isolation policy.
#### 3. In local storage (such as SharedPreferences or Room databases), use the user's unique ID (such as userId) as the data storage key.
### Expected Result:
#### 1. User data is stored separately: Different accounts can only access their own data, preventing data leakage or incorrect loading.

## Second Test Issue Description:
#### User Data Sharing
### Testing Process:
#### 1. Launch the app and enter the login page.
#### 2. Enter your phone number/email address in the "Account" field and log in.
#### 3. Log out.
#### 4. Log back in.
#### 5. Log out again and try logging in remotely.
### Expected Results:
#### Users can only access data for their specific account.
#### Users can download user data from the cloud when logging in remotely.
### Result Description:
#### Testing fixed the database sharing issue between different users.
#### Users reported that previously saved reservations, addresses, and payment information were not available when logging in with the same account on different devices.
### Impact: 
#### Users were unable to access previously saved information on the new device.
### Cause Analysis:
#### User data was not synced to the cloud or was not retrieved after login, and was only stored in the local database.
### Possible Actions:
#### • Check local and cloud data when the user logs in, and synchronize as needed (e.g., compare lastSyncTime).
#### • Upload data to the cloud in real time when it changes (e.g., adding a new reservation, modifying an address).
### Expected Results:
#### 1. When a user logs in on a new device, their reservation history, address, and payment information stored in the cloud are automatically loaded.
#### 2. Offline support: Data is available when offline and automatically synchronized when connected.

## Third Test: 
#### Issue Description: When logging in with the same account on different devices, previously saved reservation history, address, and payment information are not available.
### Testing Process:
#### 1. Log in to account A on device A and complete a reservation.
#### 2. Log in to the same account on device B.
#### 3. Check user information and historical data.
### Expected Results: 
#### Reservation history and account information should be synchronized across devices.
#### Account information is successfully synchronized across devices.

# II: Test UI
## First Test Process:
#### 1. Launch the app and access the page.
#### 2. Use the appointment function to make an appointment.
#### 3. Access the pending appointment screen to delete the previous appointment.
### Expected Result: 
#### Successfully schedule and delete the appointment.
### Result Description:
### User feedback: 
#### The item icon is unclear and completely unreadable.
### Impact: 
#### Impact on user experience.
### Cause Analysis:
#### 1. Low-resolution image assets may be used (e.g., no xxxhdpi adapter provided), resulting in blurry display on HD screens.
#### 2. In the layout file, the icon's android:width/android:height settings are too small, resulting in loss of detail.
### Possible Actions:
#### 1. Use vector graphics.
#### 2. Explicitly set the appropriate size in the layout (24dp x 24dp).
### Expected Results:
#### 1. Icons are clearly visible: Displays in high definition on all screen densities and modes.
#### 2. Improved operational recognition: Users can quickly identify the entry for the appointment/deletion function.

## Second Test Issue Description:
#### Icons are unclear
### Testing Process:
#### 1. Launch the app and enter the page
#### 2. Use the appointment function to make an appointment
#### 3. Enter the pending appointment screen and delete the previous appointment
### Expected Results:
#### Icons are clearly visible
### Result Description：
#### The unclear icon issue has been fixed.
#### User feedback indicated that the font on the main interface was not centered and looked unsightly.
### Impact：
#### Impacted user experience
### Cause Analysis:
#### 1. Improper text margin settings
#### 2. ConstraintLayout was used but the centering properties of the TextView were not correctly constrained.
### Possible Actions:
#### • Optimize layout constraints
#### • Use Android Studio's Layout Inspector to check the display effect on different screen sizes.
### Expected Results:
#### 1. The text is perfectly centered and remains centered across all screen sizes and locales.
#### 2. Visual consistency is improved, and users no longer experience discomfort due to layout misalignment.

## Test 3: Issue Description
#### Text not centered
### Testing Process
#### 1. Launch the app and enter the page
#### 2. Use the appointment function to make a reservation
#### 3. Access the pending appointment screen to delete the previous reservation
### Expected Result
#### The text is centered on the button, and the margins adapt to the screen size.
### Actual Result
#### The text is centered on the button, and the margins adapt to the screen size.
