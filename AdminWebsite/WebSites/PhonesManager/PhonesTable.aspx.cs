using Npgsql;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

public partial class PhonesTable : System.Web.UI.Page
{
    string ConnectoinString = "Server=persodb.c9c4ima6hezo.eu-central-1.rds.amazonaws.com;Port=5432; Userid=postgres;Password=postgres;Database=postgres";
    public static DataSet ourDataSet = new DataSet();
    public static NpgsqlDataAdapter ourAdapter = null;
    protected void Page_Load(object sender, EventArgs e)
    {
        
        if (!IsPostBack)
        {
            ourDataSet = new DataSet();
            ourAdapter = null;
            BindData();
        }

    }

    public override void VerifyRenderingInServerForm(Control control)
    {
        /* Confirms that an HtmlForm control is rendered for the specified ASP.NET
           server control at run time. */
    }

    public void BindData()
    {
        GridView1.DataSource = null;
        GridView1.DataSource = GetPhonesData();
        GridView1.DataBind();        
    }

    public DataTable GetPhonesData()
    {                
        using (NpgsqlConnection connection = new NpgsqlConnection(ConnectoinString))
        {
            ourDataSet = new DataSet();
            try
            {                
                ourAdapter = CreatePhonesAdapter(connection, null);
                ourAdapter.Fill(ourDataSet, "phones");

                DataRow NewRow = ourDataSet.Tables["phones"].NewRow();
                NewRow["id"] = -1;
                NewRow["name"] = "Enter new phone name";
                ourDataSet.Tables["phones"].Rows.Add(NewRow);
            }
            catch (Exception ex)
            {
                Response.Write(ex.ToString());
            }
        }

        return ourDataSet.Tables["phones"];

    }

    public DataTable GetPhonesDataPage(int pageNumber)
    {
        using (NpgsqlConnection connection = new NpgsqlConnection(ConnectoinString))
        {
            ourDataSet = new DataSet();
            try
            {
                ourAdapter = CreatePhonesAdapter(connection, pageNumber);
                ourAdapter.Fill(ourDataSet, "phones");

                DataRow NewRow = ourDataSet.Tables["phones"].NewRow();
                NewRow["id"] = -1;
                NewRow["name"] = "Enter new phone name";
                ourDataSet.Tables["phones"].Rows.Add(NewRow);
            }
            catch (Exception ex)
            {
                Response.Write(ex.ToString());
            }
        }

        return ourDataSet.Tables["phones"];

    }

    public static NpgsqlDataAdapter CreatePhonesAdapter(
    NpgsqlConnection connection, int? page)
    {
        string query;
        if (page != null)
        {
            query = @"SELECT id, name, os, ram, cpu_cores, storage, camera, gps, 
                                            battery, screen_resolution, price, screen_size ,image_url
                                    FROM phones order by id limit 15 offset " + (page - 1) * 15;
        }
        else
        {
            query = @"SELECT id, name, os, ram, cpu_cores, storage, camera, gps,  
                                            battery, screen_resolution, price, screen_size,image_url
                                    FROM phones order by id";

        }
        // Create the SelectCommand.
        NpgsqlDataAdapter adapter = 
            new NpgsqlDataAdapter(query, connection);

        NpgsqlCommandBuilder command = new NpgsqlCommandBuilder(adapter);

        adapter.InsertCommand = command.GetInsertCommand();

        adapter.UpdateCommand = command.GetUpdateCommand();

        adapter.DeleteCommand = command.GetDeleteCommand();

        return adapter;
    }

    private void deleteRowByID(int id)
    {
        using (NpgsqlConnection connection = new NpgsqlConnection(ConnectoinString))
        {
            try
            {
                connection.Open();
                NpgsqlCommand command = new NpgsqlCommand("DELETE FROM phones where id = @id", connection);

                command.Parameters.AddWithValue("@id", NpgsqlTypes.NpgsqlDbType.Integer, id);

                command.ExecuteNonQuery();
                connection.Close();
            }
            catch (Exception ex)
            {
                Response.Write(ex.ToString());
            }
        }
    }
    private void insertRow(System.Collections.Specialized.OrderedDictionary keyArray)
    {
        using (NpgsqlConnection connection = new NpgsqlConnection(ConnectoinString))
        {
            try
            {
                connection.Open();
                NpgsqlCommand command = new NpgsqlCommand(
                    @"INSERT INTO phones (name, os, ram, cpu_cores, storage, camera, gps, image_url,
            battery, screen_resolution, price, screen_size)
                    VALUES (@Name, @Os, @Ram, @cores, @storage, @camera, @gps,
                            @image_url, @battery, @screen_resolution, @price, @screen_size);",      
                    connection);

                command.Parameters.AddWithValue("@Name", NpgsqlTypes.NpgsqlDbType.Text, keyArray[0] ?? DBNull.Value);
                command.Parameters.AddWithValue("@Os", NpgsqlTypes.NpgsqlDbType.Text, keyArray[1] ?? DBNull.Value);
                command.Parameters.AddWithValue("@Ram", NpgsqlTypes.NpgsqlDbType.Bigint, keyArray[2] ?? DBNull.Value);
                command.Parameters.AddWithValue("@cores", NpgsqlTypes.NpgsqlDbType.Bigint, keyArray[3] ?? DBNull.Value);
                command.Parameters.AddWithValue("@storage", NpgsqlTypes.NpgsqlDbType.Bigint, keyArray[4] ?? DBNull.Value);
                command.Parameters.AddWithValue("@camera", NpgsqlTypes.NpgsqlDbType.Text, keyArray[5] ?? DBNull.Value);
                command.Parameters.AddWithValue("@gps", NpgsqlTypes.NpgsqlDbType.Boolean, keyArray[6] ?? DBNull.Value);

                command.Parameters.AddWithValue("@battery", NpgsqlTypes.NpgsqlDbType.Text, keyArray[7] ?? DBNull.Value);
                command.Parameters.AddWithValue("@screen_resolution", NpgsqlTypes.NpgsqlDbType.Text, keyArray[8] ?? DBNull.Value);
                command.Parameters.AddWithValue("@price", NpgsqlTypes.NpgsqlDbType.Integer, keyArray[9] ?? DBNull.Value);
                command.Parameters.AddWithValue("@screen_size", NpgsqlTypes.NpgsqlDbType.Double, keyArray[10] ?? DBNull.Value);

                command.Parameters.AddWithValue("@image_url", NpgsqlTypes.NpgsqlDbType.Text, keyArray[11] ?? DBNull.Value);

                command.ExecuteNonQuery();
                connection.Close();
            }
            catch (Exception ex)
            {
                Response.Write(ex.ToString());
            }
        }
    }
    private void UpdateRowByID(int id, System.Collections.Specialized.OrderedDictionary keyArray)
    {
        if (id == -1)
        {
            insertRow(keyArray);
            return;
        }
        using (NpgsqlConnection connection = new NpgsqlConnection(ConnectoinString))
        {
            try
            {
                connection.Open();
                NpgsqlCommand command = new NpgsqlCommand(
                    @"UPDATE phones 
                    SET name=@Name, os=@Os, ram=@Ram, cpu_cores=@cores, storage=@storage, camera=@camera, 
                        gps=@gps,image_url=@image_url, battery=@battery, screen_resolution=@screen_resolution, 
                        price=@price, screen_size=@screen_size
                    WHERE id = @id;",
                    connection);

                command.Parameters.AddWithValue("@id", NpgsqlTypes.NpgsqlDbType.Integer, id);
                command.Parameters.AddWithValue("@Name", NpgsqlTypes.NpgsqlDbType.Text, keyArray[0] ?? DBNull.Value);
                command.Parameters.AddWithValue("@Os", NpgsqlTypes.NpgsqlDbType.Text, keyArray[1] ?? DBNull.Value);
                command.Parameters.AddWithValue("@Ram", NpgsqlTypes.NpgsqlDbType.Bigint, keyArray[2] ?? DBNull.Value);
                command.Parameters.AddWithValue("@cores", NpgsqlTypes.NpgsqlDbType.Bigint, keyArray[3] ?? DBNull.Value);
                command.Parameters.AddWithValue("@storage", NpgsqlTypes.NpgsqlDbType.Bigint, keyArray[4] ?? DBNull.Value);
                command.Parameters.AddWithValue("@camera", NpgsqlTypes.NpgsqlDbType.Text, keyArray[5] ?? DBNull.Value);
                command.Parameters.AddWithValue("@gps", NpgsqlTypes.NpgsqlDbType.Boolean, keyArray[6] ?? DBNull.Value);
                command.Parameters.AddWithValue("@battery", NpgsqlTypes.NpgsqlDbType.Text, keyArray[7] ?? DBNull.Value);
                command.Parameters.AddWithValue("@screen_resolution", NpgsqlTypes.NpgsqlDbType.Text, keyArray[8] ?? DBNull.Value);
                command.Parameters.AddWithValue("@price", NpgsqlTypes.NpgsqlDbType.Integer, keyArray[9] ?? DBNull.Value);
                command.Parameters.AddWithValue("@screen_size", NpgsqlTypes.NpgsqlDbType.Double, keyArray[10] ?? DBNull.Value);

                command.Parameters.AddWithValue("@image_url", NpgsqlTypes.NpgsqlDbType.Text, keyArray[11] ?? DBNull.Value);
                command.ExecuteNonQuery();
                connection.Close();
            }
            catch (Exception ex)
            {
                Response.Write(ex.ToString());
            }
        }
    }

    protected void GridView1_RowDeleting(object sender, GridViewDeleteEventArgs e)
    {
        int id = Convert.ToInt32(GridView1.DataKeys[e.RowIndex].Values[0].ToString());
        deleteRowByID(id);
        BindData();
    }

    protected void GridView1_RowUpdating(object sender, GridViewUpdateEventArgs e)
    {
        int id = Convert.ToInt32(GridView1.DataKeys[e.RowIndex].Values[0].ToString());
        UpdateRowByID(id,((System.Collections.Specialized.OrderedDictionary)e.NewValues));
        GridView1.EditIndex = -1;
        BindData();

    }
    protected void GridView1_RowEditing(object sender, GridViewEditEventArgs e)
    {
        GridView1.EditIndex = e.NewEditIndex;
        BindData();
    }
    protected void GridView1_RowCancelingEdit(object sender,
                              GridViewCancelEditEventArgs e)
    {
        GridView1.EditIndex = -1;
        BindData();
    }

    protected void GridView1_DataBound(object sender, EventArgs e)
    {
       
    }

    protected void GridView1_RowDataBound(object sender, GridViewRowEventArgs e)
    {
        //if (((GridView)sender).Rows.Count > 0)
            //((GridView)sender).Rows[0].Cells[8].Width = 40;
    }

    protected void GridView1_PageIndexChanged(object sender, EventArgs e)
    {

    }

    protected void GridView1_PageIndexChanging(object sender, GridViewPageEventArgs e)
    {
    }
}
