package info.serveros;

import java.security.PublicKey;
import java.security.PrivateKey;
import javax.json.JsonValue;
import info.serveros.algorithms.*;
import info.serveros.messages.TicketAck;
import info.serveros.messages.TicketId;
import info.serveros.messages.TicketPresentation;
import info.serveros.messages.Ticket;
import info.serveros.messages.EncipheredAck;
import info.serveros.exceptions.*;
import javax.json.JsonObject;

/**
 *  A Service Provider in the Serveros infrastructure.
 */
public class ServerosProvider extends Encrypter {

    /**
     *  ID.  Who am I?
     */
    public final JsonValue id;

    /**
     *  The Public Key of the Authentication Master.
     */
    public final PublicKey masterPublicKey;

    /**
     *  My Private Key.
     */
    public final PrivateKey myPrivateKey;

    /**
     *  Constructor.
     *
     *  @param id Who am I?
     *  @param masterPublicKey the public RSA key of the Master server.
     *  @param myPrivateKey the private key of this provider.
     *  @param hashPrefs supported Hashes in order of preference.
     *  @param cipherPrefs supported Ciphers, in order of preference.
     */
    public ServerosProvider(JsonValue id, PublicKey masterPublicKey
                , PrivateKey myPrivateKey, HashSpec[] hashPrefs, CipherSpec[] cipherPrefs
            ) {
        super(cipherPrefs, hashPrefs);
        this.id = id;
        this.masterPublicKey = masterPublicKey;
        this.myPrivateKey = myPrivateKey;
    }//ServerosProvider(JsonValue, PublicKey, PrivateKey, HashSpec[], CipherSpec[])*/

    /**
     *  Validate the Ticket/Id.
     *
     *  @param ticket the ticket from the Authentication Master.
     *  @param id The ID sent from the requester.
     *
     *  @throws NonceMismatchException If one of the nonces does not match.
     *  @throws StaleRequestException If the Ticket is stale.
     */
    private void validate(Ticket ticket, TicketId id ) throws NonceMismatchException, StaleRequestException {
        if (ticket.isStale()) throw new StaleRequestException(ticket.getTimestamp());
        if (ticket.requesterNonce != id.requesterNonce) throw new NonceMismatchException("Requester");
        if (ticket.serverNonce != id.serverNonce) throw new NonceMismatchException("Server");
    }//validate(Ticket, TicketId)*/

    /**
     *  Get the ticket out of the TicketPresentation.
     *
     *  @param p The TicketPresentation Message from the Requester.
     *
     *  @return the ticket sent by the Authentication Master.
     *
     *  @throws GeneralSecurityException if something goes wrong with Decryption.
     *  @throws VerificationException If the signature is messy.
     */
    private Ticket getTicket(TicketPresentation p)
                throws java.security.GeneralSecurityException
                    , VerificationException
            {
        return new Ticket(this.decryptAndVerify(this.myPrivateKey, this.masterPublicKey, p.ticket));
    }//getTicket(TicketPresentation)*/

    private TicketId getId(TicketPresentation p, Ticket ticket)
                throws java.security.GeneralSecurityException
            {
        return new TicketId(this.decipher(p.id, ticket.oneTimeCredentials));
    }

    /**
     *  Get an Ack response to the TicketPresentation.
     *
     *  @throws NonceMismatchException If one of the nonces does not match.
     *  @throws StaleRequestException If the Ticket is stale.
     *  @throws GeneralSecurityException if something goes wrong with Decryption.
     *  @throws VerificationException If the signature is messy.
     */
    public EncipheredAck getEncipheredAck(TicketPresentation p)
                throws java.security.GeneralSecurityException
                    , MessageException
            {
        Ticket ticket = this.getTicket(p);
        TicketId id = this.getId(p, ticket);

        this.validate(ticket, id);
        TicketAck ack = new TicketAck(
            ticket.requesterNonce
            , ticket.serverNonce
            , id.finalNonce
        );

        return new EncipheredAck(this.encipher(ack, new OneTimeCredentials(
            ticket.oneTimeCredentials.getKeyString()
            , id.iv
            , ticket.oneTimeCredentials.cipher
            , ticket.oneTimeCredentials.hash
        )));
    }

    public EncipheredAck getEncipheredAck(String id, String message, String signature)
                throws java.security.GeneralSecurityException
                    , MessageException
            {
        return this.getEncipheredAck(new TicketPresentation(id, new CryptoMessage(message, signature, null)));
    }

    public EncipheredAck getEncipheredAck(String ticketPresentation)
                throws java.security.GeneralSecurityException
                    , MessageException
            {
        return this.getEncipheredAck(new TicketPresentation(ticketPresentation));
    }

    public EncipheredAck getEncipheredAck(JsonObject ticketPresentation)
                throws java.security.GeneralSecurityException
                    , MessageException
            {
        return this.getEncipheredAck(new TicketPresentation(ticketPresentation));
    }

    public Credentials getCredentials(TicketPresentation p)
                throws java.security.GeneralSecurityException
                    , MessageException
            {
        Ticket ticket = this.getTicket(p);
        TicketId id = this.getId(p, ticket);

        this.validate(ticket, id);
        return new Credentials(
            ticket.requester
            , ticket.id
            , ticket.secret
            , ticket.hash
            , ticket.getExpiry()
            , ticket.authData
        );
    }
}
