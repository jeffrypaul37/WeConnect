package com.webapp.weconnect.controller;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.model.checkout.Session;

import com.webapp.weconnect.model.Donation;
import com.webapp.weconnect.model.Event;
import com.webapp.weconnect.model.User;
import com.webapp.weconnect.repository.EventRepository;
import com.webapp.weconnect.repository.UserRepository;
import com.webapp.weconnect.service.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class DonationController {

    @Value("${base.url}")
    private String baseUrl;

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    private String priceID = "price_1Ov24MEkAeeAylyBh4C2deFz";

    @Autowired
    private final DonationService donationService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    public DonationController(DonationService donationService, UserRepository userRepository) {
        this.donationService = donationService;
        this.userRepository = userRepository;
    }

    /**
     * Method to save the response of the donation response form available in the events.
     *  So that a log of donors can be kept in the database for future reference as well as to display it on the donations tab.
     * @param description the details which the donor needs to mention
     * @param resourceType the type of donation being made
     * @param quantity the amount which they want to donate
     * @param eventTitle the event that is associated
     * @param communityName the community which hosts the event
     * @return back to the page
     * */
    @PostMapping("/donate_resource")
    public String donateResource(@RequestParam("description") String description,
                                 @RequestParam("resourceType") String resourceType,
                                 @RequestParam("quantity") int quantity,
                                 @RequestParam("eventTitle") String eventTitle,
                                 @RequestParam("communityName") String communityName,
                                 HttpServletRequest request)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String donorEmail = authentication.getName();
        // Find the user by email
        User currentUser = userRepository.findByEmail(donorEmail);
        Donation donation = new Donation();
        donation.setDescription(description);
        donation.setResourceType(resourceType);
        donation.setQuantity(quantity);
        donation.setUser(currentUser);
        donation.setEventTitle(eventTitle);
        donation.setCommunityName(communityName);

        // Save the donation using the DonationService
        donationService.saveDonation(donation);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    /**
     * Method to redirect the donor to the stripe hosted page for making monry donation
     * So that the user can donate money using his preferred mode of payment.
     * @param eventTitle the event for which the donation is made
     * @param communityName the community in which the event is there and donation is made
     * @param response the HttpResponse
     * */
    @GetMapping("/donate")
    public void redirectToStripeCheckout(@RequestParam("eventTitle") String eventTitle,
                                         @RequestParam("communityName") String communityName,
                                         HttpServletResponse response) {
        Stripe.apiKey = stripeSecretKey;
        try {
            Session session = createStripeSession(eventTitle, communityName);
            response.sendRedirect(session.getUrl());
        } catch (StripeException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to create a stripe session before initiating the transaction on stripe.
     *  So that each transaction is a new transaction so as to prevent any cyber fraud.
     * @param eventTitle the event for which the donation is made
     * @param communityName the community in which the event is there and donation is made
     * @return the created stripe session with the associated parameters.
     * */
    /* Package-private method for creating a Stripe session */
    public Session createStripeSession(String eventTitle, String communityName) throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(baseUrl + "/success?sessionId={CHECKOUT_SESSION_ID}")
                .setCancelUrl(baseUrl + "/cancel")
                .setSubmitType(SessionCreateParams.SubmitType.DONATE)
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPrice(priceID)
                        .build())
                .putMetadata("event_title", eventTitle)
                .putMetadata("community_name", communityName)
                .build();
        return Session.create(params);
    }

    /**
     * Method to handle the post transaction success page which contains the transaction details.
     * So that the user can have it as a record for the payment made.
     * @param request the HttpServlet request
     * @param model the model parameter
     * @return success page on successful transaction, error page otherwise.
     * */
    @GetMapping("/success")
    public String handleSuccess(HttpServletRequest request, Model model) {
        String sessionId = request.getParameter("sessionId");
        try {
            Session session = Session.retrieve(sessionId);
            String paymentIntentId = session.getPaymentIntent();
            Long amountPaid = session.getAmountTotal();
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            String modeOfPayment = paymentIntent.getPaymentMethodTypes().get(0); // Assuming only one payment method type
            String customerName = session.getCustomerDetails().getName();
            String eventTitle = session.getMetadata().get("event_title");
            String communityName = session.getMetadata().get("community_name");
            // Add transaction details to the model
            model.addAttribute("transactionId", paymentIntentId);
            model.addAttribute("amount", amountPaid);
            model.addAttribute("modeOfPayment", modeOfPayment);
            model.addAttribute("donorName", customerName);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String donorEmail = authentication.getName();
            // save the donation to the database for record
            User currentUser = userRepository.findByEmail(donorEmail);
            Donation donation = new Donation();
            donation.setDescription("money_donation");
            donation.setResourceType(modeOfPayment);
            donation.setQuantity(1);
            donation.setUser(currentUser);
            donation.setEventTitle(eventTitle);
            donation.setCommunityName(communityName);
            donationService.saveDonation(donation);

            return "success";
        } catch (StripeException e) {
            e.printStackTrace();
            return "error";
        }
    }

}
