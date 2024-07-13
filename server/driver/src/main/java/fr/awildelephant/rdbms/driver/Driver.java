package fr.awildelephant.rdbms.driver;

import fr.awildelephant.rdbms.jdbc.AbstractRDBMSDriver;
import fr.awildelephant.rdbms.jdbc.abstraction.ServerProxy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public final class Driver extends AbstractRDBMSDriver {

    private static final Pattern URL_PATTERN = Pattern.compile("jdbc:rdbms://([^:]+):(\\d+)/(.*)");

    @Override
    protected ServerProxy createProxy(String url) {
        final Matcher matcher = URL_PATTERN.matcher(url);

        if (!matcher.matches()) {
            throw new IllegalArgumentException();
        }

        final String host = matcher.group(1);
        final int port = parseInt(matcher.group(2));

        return new RemoteServer(host, port);
    }

    @Override
    protected boolean acceptsNotNullURL(String url) {
        return URL_PATTERN.matcher(url).matches();
    }
}
