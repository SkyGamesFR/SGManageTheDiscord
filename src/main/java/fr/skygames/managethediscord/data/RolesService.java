package fr.skygames.managethediscord.data;

import fr.skygames.managethediscord.sql.SqlConnector;
import fr.skygames.managethediscord.utils.exception.MissingPropertyException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RolesService {

    private final SqlConnector connector;

    public RolesService(SqlConnector connector) {
        this.connector = connector;
    }

    public static Roles getRolesFromResultSet(ResultSet rs) throws SQLException {
        return new Roles(rs.getString("id"), rs.getString("name"), rs.getBoolean("isStaff"));
    }

    public Roles get(final ResultSet rs) throws SQLException {
        if(rs.first()) {
            return getRolesFromResultSet(rs);
        }else {
            return null;
        }
    }

    public Roles getRoleFromID(final String id) throws SQLException, ClassNotFoundException, MissingPropertyException {
        PreparedStatement stmt = this.connector.getConnection().prepareStatement("{CALL getRoleFromID(?)}");
        stmt.setString(1, id);
        return get(stmt.executeQuery());
    }

    public Roles getRoleFromName(final String name) throws SQLException, ClassNotFoundException, MissingPropertyException {
        PreparedStatement stmt = this.connector.getConnection().prepareStatement("{CALL getRoleFromName(?)}");
        stmt.setString(1, name);
        return get(stmt.executeQuery());
    }

    public Roles getAll() throws SQLException, ClassNotFoundException, MissingPropertyException {
        PreparedStatement stmt = this.connector.getConnection().prepareStatement("{CALL getAllRoles()}");
        return get(stmt.executeQuery());
    }

    public void create(final Roles roles) throws SQLException, MissingPropertyException, ClassNotFoundException {
        PreparedStatement stmt = this.connector.getConnection().prepareStatement("{CALL addRole(?,?)}");
        stmt.setString(1, roles.getId());
        stmt.setString(2, roles.getName());
        stmt.executeUpdate();
    }

    public void delete(final String id) throws SQLException, MissingPropertyException, ClassNotFoundException {
        PreparedStatement stmt = this.connector.getConnection().prepareStatement("{CALL deleteRole(?)}");
        stmt.setString(1, id);
        stmt.executeUpdate();
    }

    public Roles setStaffFromID(final String id, final Boolean isStaff) throws SQLException, MissingPropertyException, ClassNotFoundException {
        PreparedStatement stmt = this.connector.getConnection().prepareStatement("{CALL setStaffFromID(?,?)}");
        stmt.setString(1, id);
        stmt.setBoolean(2, isStaff);
        stmt.executeUpdate();
        return getRoleFromID(id);
    }
}
