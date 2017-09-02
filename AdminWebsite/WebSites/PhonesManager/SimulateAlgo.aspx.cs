using Npgsql;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

public partial class SimulateAlgo : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {

    }
    string ConnectoinString = "Server=persodb.c9c4ima6hezo.eu-central-1.rds.amazonaws.com;Port=5432; Userid=postgres;Password=postgres;Database=postgres";
    protected void btnSubmit_Click(object sender, EventArgs e)
    {
        try
        {
            int userId, battery, cpu, ram, storage, camera;
            if (int.TryParse(txtUserID.Text, out userId) &&
                int.TryParse(txtBattery.Text, out battery) &&
                int.TryParse(txtCPU.Text, out cpu) &&
                int.TryParse(txtRam.Text, out ram) &&
                int.TryParse(txtStorage.Text, out storage) &&
                int.TryParse(txtCamera.Text, out camera))
            {
                using (NpgsqlConnection connection = new NpgsqlConnection(ConnectoinString))
                {
                    try
                    {
                        connection.Open();
                        NpgsqlCommand command = new NpgsqlCommand(
                            @"select * from simulate_algorithem(@userid, @battery, @cpu ,@ram, @storage, @camera);",
                            connection);

                        command.Parameters.AddWithValue("@userid", NpgsqlTypes.NpgsqlDbType.Integer, userId);
                        command.Parameters.AddWithValue("@battery", NpgsqlTypes.NpgsqlDbType.Integer, battery);                        
                        command.Parameters.AddWithValue("@cpu", NpgsqlTypes.NpgsqlDbType.Integer, cpu);
                        command.Parameters.AddWithValue("@ram", NpgsqlTypes.NpgsqlDbType.Integer, ram);
                        command.Parameters.AddWithValue("@storage", NpgsqlTypes.NpgsqlDbType.Integer, storage);
                        command.Parameters.AddWithValue("@camera", NpgsqlTypes.NpgsqlDbType.Integer, camera);
                        

                        command.ExecuteNonQuery();
                        connection.Close();
                    }
                    catch (Exception ex)
                    {
                        Response.Write(ex.ToString());
                    }
                }
            }
            else
            {
                ClientScript.RegisterStartupScript(this.GetType(), "myalert", "alert('" + "not all fields valid" + "');", true);
            }
        }
        catch (Exception ex)
        {

        }
    }
}