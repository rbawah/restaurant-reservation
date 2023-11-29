package com.project.restaurantbooking.controller;

import com.project.restaurantbooking.agent.AdminStaffAgent;
import com.project.restaurantbooking.agent.StaffAgent;
import com.project.restaurantbooking.entity.Shift;
import com.project.restaurantbooking.entity.Staff;
import com.project.restaurantbooking.messagetemplates.AddStaffResponse;
import com.project.restaurantbooking.service.StaffService;
import jade.core.*;
import jade.core.Agent;
import jade.core.Runtime;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@CrossOrigin({"*"})
public class StaffController extends Agent{

    @Autowired
    private StaffService staffService;

//    @Autowired
//    private AdminStaffAgent adminStaffAgent;

    @Autowired
    public StaffController(StaffService staffService){
        this.staffService = staffService;
    }

    @GetMapping({"/"})
    public String index(){
        return "Welcome to the restaurant staff portal. Please login to continue.";
    }

    @PostMapping("/api/v1/login")
    public CompletableFuture<Optional<Staff>> login(@RequestParam String username, @RequestParam String password) {
        CompletableFuture<Optional<Staff>> loginStaff = staffService.login(username, password);
        return loginStaff;
    }

    @PostMapping("/api/v1/adminlogin")
    public AdminStaffAgent adminLogin(@RequestParam String username, @RequestParam String password) {
//        //Create new staff agent with authenticate ability.
//        Runtime runtime = Runtime.instance();
//        Profile profile = new ProfileImpl();
//        profile.setParameter(Profile.MAIN_HOST, "localhost");
//        //change this to refer to restaurant container instead of creating main container.
//        ContainerController container = runtime.createMainContainer(profile);
//        String agentName = username + "-sa";
//
//        AdminStaffAgent adminStaff = null;
//        try {
//            AgentController agentController = container.createNewAgent(agentName, "com.project.restaurantbooking.agent.AdminStaffAgent", null);
//            //Run authenticate function on new staff agent.
//            adminStaff = (AdminStaffAgent) adminStaffAgent.authenticate(username, password);
//
//            if (adminStaff == null) {
//                //Deregister and kill the agent.
//                killStaffAgent(agentName);
//            }
//
//        } catch (StaleProxyException e) {
//            e.printStackTrace();
//        }
//
//        //Return staff agent if successful.
//        return adminStaff;
        return null;
    }

    @PostMapping("/api/v1/logout")
    public void logout(@RequestParam String username){
        String agentName = username + "-sa";
        killStaffAgent(agentName);
    }

    @PostMapping("api/v1/addstaff")
    public CompletableFuture<AddStaffResponse> addStaff(@RequestParam String username, @RequestBody Staff newStaff){
        return staffService.addStaff(username, newStaff);
    }

    @PostMapping("api/v1/deletestaff")
    public void deleteStaff(@RequestParam(required = false) Long id, @RequestParam(required = false) String username){
//        if(id != null){
//            this.adminStaffAgent.deleteStaffById(id);
//        }
//        else if(username != null){
//            this.adminStaffAgent.deleteStaffByUsername(username);
//        }
//        else {
//            throw new IllegalArgumentException("No staff information provided.");
//        }
    }

    @PostMapping("api/v1/changestaff")
    public void changeStaff(@RequestParam Long staffID,
                            @RequestParam(required = false) String newFirstName,
                            @RequestParam(required = false) String newLastName,
                            @RequestParam(required = false) String newUsername,
                            @RequestParam(required = false) boolean changeAdmin,
                            @RequestParam(required = false) String newPassword){
        Long tempRestaurantID = Long.valueOf(1);
        Staff staffChange = new Staff(null, newFirstName, newLastName, newUsername, changeAdmin, newPassword);
//        adminStaffAgent.changeStaffAttributes(staffID, staffChange);

    }

    @PostMapping("api/v1/addtable")
    public void createTable(@RequestParam Long restaurantId,
                            @RequestParam int tableOccupancyNum,
                            @RequestParam boolean available,
                            @RequestParam(required = false) ArrayList<Shift> timeslots){
        if(timeslots == null){
            //use empty timeslots
//            adminStaffAgent.createEmptyTable(restaurantId, tableOccupancyNum, available);
        }
        else{
//            adminStaffAgent.createTable(restaurantId, tableOccupancyNum, available, timeslots);
        }
    }

    @PostMapping("api/v1/deletetable")
    public void deleteTable(){

    }

    private void killStaffAgent(String agentName){
        ACLMessage killMsg = new ACLMessage((ACLMessage.INFORM));
        killMsg.addReceiver(new AID(agentName, AID.ISLOCALNAME));
        killMsg.setContent("terminate");
        send(killMsg);
    }

}
