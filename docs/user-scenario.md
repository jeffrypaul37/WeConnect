# User Scenarios 

  

## Features 

- Freedom to create community of their own 

- Explore the available communities 

- Interact with other members of community 

- Offer donations for events 

- Admin Approves communities 

- Show Blogs posted by Community Leader 

- Update Personal Information 

- Create Events for community

- Leader modifies the community and event details

  

  

## Scenarios 

  
1. **User wants to use `WeConnect` website**
    <br><br>
    The user needs to register on the website if they wants to use the features of WeConnect. Unless, a user registers on the website they won't be able to use any features.

    
2. **User logs in to `WeConnect` website** 
    <br><br>
    The user can log in to the website using their credentials.Once authenticated, they are sent to dashboard.The dashboard contains list of available communities to explore as well as navigation tabs to toggle between features. The user can see:

    - User profile 

    - About Page of Company 

    - View joined, created as well as pending approval for communities 


3. **User forgets password and wants to reset password**
    <br><br>
    User forgets their password and wants to create a new one. They can click on forgot password link and enter their registered email address to receive a link. Using that link they can go to reset password page and set a new one.    


4. **User wants to reset password**
    <br><br>
    User clicks on password reset link sent to them on their registered email address. This link will take them to reset password page where new password is created.


5. **User wants to create a community of his/her own** 
    <br><br>
    When the user clicks on "My Communities" he can click on "Create Community" button and enter details about the community and send request for approval to Admin 
    <br><br>
    Each form will contain below details: 

   - Community Name
   - Description
   - Community Image
   - ID Proof of User for verification 
    
    The name of the community should be unique, communities with same name cannot exist. The approval of community depends on the Admin. A community will be approved based on the ID proof that the community leader provides during community creation. 
    <br><br>
    Once the community is approved, it is visible to other users of the website. 

  

6. **User wants to explore a community**
    <br><br>
    When a user clicks on a community, they can see the blogs posted by the leader. They may comment on the blog posts once they have joined the community. After joining the communities, user can see the events organized by that community and can make donations. 

   
7. **User wants to register for an event** 
    <br><br>
    When a user is interested in registering for event, he can fill out their details and can register for the event. Upon successful registration, a confirmation email will be received. Event registration can only be done if user is part of community and there are seats left to register. 


8. **Leader wants to create a new event**
    <br><br>
    When a leader wants to organize a new event, they can create a new event by entering the event details, set the capacity and upload image for event. Once created, then members can register for an event.

   
9. **User wants to donate for events** 
    <br><br>
    If a user looks forward to contribute for an event, they have two choices to make donation: 
    <br><br>
   - Donate Resources
   - Donate Money / Monetary Donation 
    
    To donate resources for an event, a user can fill out the donate resource form on the website where they can enter details about the donation being offered. Once the donation form is submitted, they can physically donate it. If user wants to donate money, then they can click on Donation Money Button where they will be redirected to payment page. There user can enter their card details and can proceed to donate money. 
    <br><br>
    Once the donation is successful, they will be displayed with details of transactions like amount, transaction ID for future reference. 

  

10. **User wants to see donations made by other users** 
    <br><br>
    If User wants to check who are the generous donors for various events, the list of donors can be seen by clicking on donations tab. 

  

11. **User wants to Search for a particular Community** 
    <br><br>
    When a User wants to look for a specific community, they can use the search filter. They can filter based on: 

   * Location 

   * Name 

   * Role 


12. **User wants to update his personal information**
    <br><br>
    User wants to update their personal details like profile photo, location, name, etc. They can change it by viewing the user profile page and click on edit profile.



### *Admin* 

  

1. **Admin wants to use the website.** 
    <br><br>
    An admin can login into the website using the admin credentials decided by the company. Once they login, they can perform the admin tasks. 


2. **Admin wants to see the list of approved communities.** 
    <br><br>
    Once logged in, they can see the list of communities are currently active in the web application.  

 

3. **Admin wants to approve a community.**  
    <br>
    The admin can click on pending request button to see the records of communities created by respective member leaders.  On clicking on a request , they  can see the leader who created it, the name of the community as well as can verify the ID proof submitted by the leader before giving approval. 

 
Back to <u>[Home Page](../README.md)</u>.