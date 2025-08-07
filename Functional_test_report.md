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
### Reproduction Procedure:
#### 1. Log in to account A on device A and complete a reservation.
#### 2. Log in to the same account on device B.
#### 3. Check user information and historical data.
### Expected Results: 
#### Reservation history and account information should be synchronized across devices.
#### Account information is successfully synchronized across devices.
