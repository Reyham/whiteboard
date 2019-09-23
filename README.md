# UoM COMP90015 Distributed Systems - Distributed Whiteboard

This document is used by team members to record requirements, technical framework and meeting minutes.

## Deadlines

#### Deadline 1
Milestone 1 (Progress Review): **Week 10, Tuesday (Oct. 8)** in our own tutorial.

+ 4 marks will be given for demonstrating progress in phase 1.  
    * Requirement A of Phase 1: 2 marks
        - Implement a client that allows a single user to draw all the expected elements (line, circle, rectangle, oval, freehand drawing, and erasing).
    * Requirement B of Phase 1: 2 marks
        - Implement the open, new, save, save as, and close functionality for a single client.


#### Deadline 2
Milestone 2 (Final Submission): **Week 12, Friday (Oct 25)** at 5:00pm.

Write a report that includes the system architecture, communication protocols and message formats, design diagrams (class and interaction), implementation details, new innovations, and a section that outlines the contribution of each member.

| Name & Std. No.  | Contribution area | Overall contribution (% out of 100) to Project |
| ------------- | ------------- | ------------- |
| ..  | Describe…  | 20%? (decide reasonably) |
| ...  | ... | 15%? |

+ Submit the following via LMS:**
    - Report in PDF format only.
    - The executable jar files used to run system’s clients/server(s)
    - Source files in a .ZIP or .TAR archive only.

## Requirements

An initial version of [guidelines](Project2-2019.pdf) can be found in this repo.

Shared whiteboards allow multiple users to draw simultaneously on a canvas. The system should support a range of features such as freehand drawing with the mouse, drawing lines and shapes such as circles and squares that can be moved and resized, and inserting text. In addition to these features, the implementation should include a simple chat window, that allows all the current users of the system to broadcast messages to each other.

#### Function List

+ GUI Elements:
    - Shapes
        * [ ] Line
        * [ ] Circle
        * [ ] Rectangle
        * [ ] Oval
    - [ ] Free Draw
    - [ ] Erase
    - [ ] Text Inputting (Allow user to type text everywhere inside the whiteboard)
    - [ ] Color Select (At least 16 colors)
    - [ ] Chat Window (Text based)
    - File Menu
        * [ ] New
        * [ ] Open
        * [ ] Save
        * [ ] SaveAs
        * [ ] Close
+ Clients:
    - **Unique Username** - Users must provide a username when joining the whiteboard. There should be a way of uniquely identifying users, either by enforcing unique usernames or automatically generating a unique identifier and associating it with each username.
    - **Equivalent Privileges** - All the users should see the same image of the whiteboard and should have the privilege of doing all the drawing operations.
    - **Live Users** - When displaying a whiteboard, the client user interface should show the usernames of other users who are currently editing the same whiteboard.
    - **State Acquisition** - Clients may connect and disconnect at any time. When a new client joins the system the client should obtain the current state of the whiteboard so that the same objects are always displayed to every active client.
    - **Manager to Manage** - Only the manager of the whiteboard should be allowed to create a new whiteboard, open a previously saved one, save the current one, and close the application.
+ Proposed Operational Model
    - The first user creates a whiteboard and becomes the whiteboard’s manager
    ```bash
    java CreateWhiteBoard <serverIPAddress> <serverPort> username
    ```
    - Other users can ask to join the whiteboard application any time by inputting server’s IP address and port number
    ```bash
    java JoinWhiteBoard <serverIPAddress> <serverPort> username
    ```
    - A notification will be delivered to the manager if any peer wants to join. The peer can join in only after the manager approves
    - **An online peer list** should be maintained and displayed
    - All the peers will see the identical image of the whiteboard, as well as have the privilege of doing all the operations (draw and erase)
    - Online peers can **choose to leave** whenever they want. **The manager can kick someone out** at any time.
    - When the **manager quits**, the application will be terminated. All the peers will get a message notifying them.



## Teamwork

#### Division of Responsibilities

+ Client 
    - Reyham Soenasto
    - Rudolph Almeida
    - Steven Peng
+ Server
    - Boyang Yue

#### Git Workflow

[Forking Workflow](https://www.atlassian.com/git/tutorials/comparing-workflows/forking-workflow) & [Pull Request](https://www.atlassian.com/git/tutorials/making-a-pull-request)

## Technical Framework

#### Libraries Could be Used

- JavaFX (Built in Java 8): [JavaFX 13 at openjfx.io](https://openjfx.io), [JavaFX 8 at docs.oracle.com](https://docs.oracle.com/javase/8/javafx/api/index.html)
    - JFoenix (Material Design): [Official Website](http://www.jfoenix.com), [Github](https://github.com/jfoenixadmin/JFoenix)
- Java2D drawing package: [Docs at Oracle](https://docs.oracle.com/javase/tutorial/2d/index.html)

#### System Architecture

To Be Done

<br>

**Have fun and start from now!**