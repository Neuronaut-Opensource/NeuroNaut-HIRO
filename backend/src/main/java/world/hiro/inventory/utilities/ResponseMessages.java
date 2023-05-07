package world.hiro.inventory.utilities;

public class ResponseMessages {
    // PingController
    public static String systemIsWorkingMessage = "Home Inventory Service is functioning as expected.";
    // InventoryController
    public static String failedToFindAllInventoryItems = "Failed to find all inventory within the given household.";
    public static String failedToFindInventoryItemById = "Failed to find inventory item by ID.";
    public static String failedToFindInventoryItemByName = "Failed to find InventoryItem by Name.";
    public static String failedToFindInventoryItemByCategory = "Failed to find InventoryItem by Catgegory.";
    public static String failedToFindInventoryItemByLocation = "Failed to find InventoryItem by Location.";
    public static String failedToFindInventoryItemByRoom = "Failed to find InventoryItem by Room.";
    public static String failedToFindInventoryItemByExpirationRange = "Failed to find InventoryItem by range of expiration dates. Please use 'YYYY-MM-DD HH:mm:ss' format.";
    public static String failedToCreateInventoryItemBadRequest = "Failed to create Inventory Item, bad request!";
    public static String succesfullyDeletedInventoryItemById = "Successfully deleted inventory item.";
    // UserController
    public static String failedToFindUserById = "Failed to find user by Id.";
    public static String failedToUpdateUserById = "Failed to update user by Id.";
    public static String succesfullyDeletedUserById = "Successfully deleted user by Id.";
    public static String failedToDeleteUserById = "Failed to delete user by Id.";
    // AuthenticationController
    public static String emailAlreadyInUse = "Failed to enroll new user. Email is already in use.";
    public static String invalidInviteCode = "The invite code used to enroll is invalid.";
    public static String enrolled (String inviteCode) { return "User enrolled successfully with invite code: " + inviteCode; }
}
