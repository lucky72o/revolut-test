package controllers;

import dtos.MoneyTransferResponse;
import forms.MoneyTransferForm;
import models.User;
import play.data.FormFactory;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import services.TransferService;
import services.UserService;

import javax.inject.Inject;
import java.util.Optional;

public class MoneyTransferController extends Controller {

    private final UserService userService;
    private final TransferService transferService;
    private final FormFactory formFactory;

    @Inject
    public MoneyTransferController(UserService userService, TransferService transferService, FormFactory formFactory) {
        this.userService = userService;
        this.transferService = transferService;
        this.formFactory = formFactory;
    }


    @Transactional
    public Result transferFunds(long id) {
        MoneyTransferForm form = formFactory.form(MoneyTransferForm.class)
                .bindFromRequest().get();

        Optional<User> fromUser = userService.findById(form.getFromUser());
        Optional<User> toUser = userService.findById(form.getToUser());

        if(!fromUser.isPresent() || !toUser.isPresent()) {
            return notFound("Transfer not possible due to user not been found.");
        }

        MoneyTransferResponse response =
                transferService.transferFunds(fromUser.get(), toUser.get(), form.getCurrency(), form.getAmount());

        if(response.isSuccessful()) {
            return ok(response.getMessage());
        } else {
            return badRequest(response.getMessage());
        }
    }


}
