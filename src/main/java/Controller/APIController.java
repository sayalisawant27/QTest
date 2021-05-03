package Controller;

import api.DefectAPI;
import api.LoginAPI;
import api.RequirementAPI;
import org.json.JSONObject;

import java.util.Scanner;

public class APIController {

    public static void main(String[] args) throws Exception {

        RequirementAPI requirementAPI;
        DefectAPI defectAPI;

        String username, password, domain;
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter domain: ");
        domain = sc.nextLine();
        System.out.println("Enter username: ");
        username = sc.nextLine();
        System.out.println("Enter password: ");
        password = sc.nextLine();

        LoginAPI loginAPI = new LoginAPI();
        JSONObject jsonObject = loginAPI.authenticate(domain, username, password);
        System.out.println("Authentication successful...\n");

        String tokenType = jsonObject.getString("token_type");
        String accessToken = jsonObject.getString("access_token");

        System.out.println("Enter project id: ");
        String projectId = sc.nextLine();
        int choice;
        String defectId, requirementId, comment;
        do {
            System.out.println("Choose one of the following for Project ID: " + projectId);
            System.out.println("1. Get multiple requirements \n2. Delete requirement \n3. Get defect \n" +
                    "4. Add comment to a defect \n5. Get all comments of a defect \n6. Exit");
            System.out.println("Enter your choice (1-6):");
            choice = sc.nextInt();
            sc.nextLine();
            switch (choice){
                case 1:
                    requirementAPI = new RequirementAPI();
                    requirementAPI.getMultipleRequirements(domain, tokenType, accessToken, projectId);
                    System.out.println("Logged requirements for project ID: " + projectId + " successfully.\n");
                    break;

                case 2:
                    System.out.println("Enter the requirement ID to delete: ");
                    requirementId = sc.nextLine();
                    requirementAPI = new RequirementAPI();
                    requirementAPI.deleteRequirement(domain, tokenType, accessToken, projectId, requirementId);
                    System.out.println("Logged requirements for project ID: " + projectId + " after deleting requirement " +
                            "having ID: " + requirementId + " successfully.\n");
                    break;

                case 3:
                    System.out.println("Enter the defect ID: ");
                    defectId = sc.nextLine();
                    defectAPI = new DefectAPI();
                    defectAPI.getDefect(domain, tokenType, accessToken, projectId, defectId);
                    System.out.println("Logged details of defect ID: " + defectId + " successfully.\n");
                    break;

                case 4:
                    System.out.println("Enter the defect ID: ");
                    defectId = sc.nextLine();
                    System.out.println("Enter the comment: ");
                    comment = sc.nextLine();
                    defectAPI = new DefectAPI();
                    defectAPI.addCommentToDefect(domain, tokenType, accessToken, projectId, defectId, comment);
                    System.out.println("New comment added to defect ID: " + defectId + " successfully.\n");
                    break;

                case 5:
                    System.out.println("Enter the defect ID: ");
                    defectId = sc.nextLine();
                    defectAPI = new DefectAPI();
                    defectAPI.getAllCommentsOfDefect(domain, tokenType, accessToken, projectId, defectId);
                    System.out.println("Logged comments of defect ID: " + defectId + " successfully.\n");
                    break;
            }
        } while (choice != 6);

        System.out.println("\nLogs for all activities created successfully. Check QTestApiLog.xls");
    }

}
