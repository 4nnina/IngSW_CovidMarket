package main.model;

import java.io.Serializable;

public abstract class Persona implements Serializable
{
    protected String nome, cognome, indirizzo, citta, telefono, email;
    protected int CAP, passwordHash;

    // Crea oggetto dal builder
    protected Persona(Builder<?> builder)
    {
        this.nome = builder.nome;
        this.cognome = builder.cognome;
        this.indirizzo = builder.indirizzo;
        this.citta = builder.citta;
        this.telefono = builder.telefono;
        this.email = builder.email;
        this.CAP = builder.CAP;
        this.passwordHash = builder.passwordHash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj.getClass() == this.getClass())
        {
            Persona other = (Persona)obj;
            return other.nome == this.nome && other.cognome == this.cognome && other.email == this.email;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return nome.hashCode() ^ cognome.hashCode() ^ email.hashCode();
    }

    // Controlla se le credenziali sono corrette per questa persona
    public abstract LoginResult validLogin(String username, String password);

    /**
     * Builder pattern per classi derivanti
     * @param <T> Classe del builder derivato
     */
    protected abstract static class Builder<T extends Builder>
    {
        protected String nome, cognome, indirizzo, citta, telefono, email;
        protected int CAP, passwordHash;

        // Crea l'oggetto finale
        abstract Persona build();

        // Restituisce l'istanza del builder specifico
        protected abstract T self();

        // ===========================================================================
        // CAMPI

        public T setNominativo(String nome, String cognome) {
            this.cognome = cognome;
            this.nome = nome;
            return self();
        }

        public T setIndirizzo(String indirizzo, String citta, int CAP) {
            this.indirizzo = indirizzo;
            this.citta = citta;
            this.CAP = CAP;
            return self();
        }

        public T setTelefono(String telefono) {
            this.telefono = telefono;
            return self();
        }

        public T setEmail(String email) {
            this.email = email;
            return self();
        }

        public T setPassword(String password) {
            this.passwordHash = password.hashCode();
            return self();
        }
    }
}
