"Help people discover Marathi/local businesses and help business owners get discovered."

"I built an AI-accessible business platform with permission-aware MCP tools, 
allowing users and business owners to discover information, receive recommendations, and perform authorized business-management actions."
Initial users

MVP features
**Regular user**
    Public User can Browse businesses
    Public User can Search text
    Public User can Filter by category/location
    Public user can View business public profile

Registered user
    Registered user can crud user profile
    Registered user can view business deals 
    Registered user can View business private profile
    Registered user can View and Contact business
    Registered user can Save/favorites
    Registered user can review business
    Registered user can rate business

**Business owner** should be registerd user

    Business Owner can Create business profile
    Business Owner can Add services
    Business Owner can Add photos
    Business Owner can Update business information
    Business Owner can See basic profile views
            count of saved by user
            count of profile views
            
Admin 
    Admin can Approve/reject CRUD businesses
    Admin can Approve/reject CRUD business reviews
    Admin can Manage categories
    Admin can CRUD users
    -- Flag/remove businesses

MVP categories
        Restaurants
        Grocery
        Professional services
        Healthcare
        Education
        Home services
        Retail
        Technology
        Other

MVP- modules
Build:
    user registration/login
    business registration
    business search
    business profile
    admin approval 




                   Flutter
                      │
                      ▼
                BConnect APIs
                      │
        ┌─────────────┼─────────────┐
        ▼             ▼             ▼
     Business       Search        Reviews
      Service       Service       Service


             AI Assistant / Agent
                     │
                     ▼
               BConnect MCP
                     │
          ┌──────────┼──────────┐
          ▼          ▼          ▼
       Search     Business    Reviews