using Npgsql;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.CompilerServices;
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
        int userId;
        int battery, CPU, RAM, Storage, Pictures;

        if ((RadioButtonList.SelectedItem != null) && (int.TryParse(txtUserID.Text, out userId)))
        {
            SetValues(out battery, out CPU, out RAM, out Storage, out Pictures);

            DeteleUser(userId);
            StimulateUser(userId, battery, CPU, RAM, Storage, Pictures);


        }
        else if((int.TryParse(txtUserID.Text, out userId) &&
                 int.TryParse(txtBattery.Text, out battery) &&
                 int.TryParse(txtCPU.Text, out CPU) &&
                 int.TryParse(txtRam.Text, out RAM) &&
                 int.TryParse(txtStorage.Text, out Storage) &&
                 int.TryParse(txtCamera.Text, out Pictures)))
        {
            DeteleUser(userId);
            StimulateUser(userId, battery, CPU, RAM, Storage, Pictures);
        }

    }

    protected void SetValues(out int battery, out int CPU, out int RAM, out int Storage, out int Pictures)
    {
        switch(RadioButtonList.SelectedItem.Value)
        {
            case "High":
                battery = 1;
                CPU = 100;
                RAM = 100;
                Storage = 100;
                Pictures = 20;
                break;

            case "Medium":
                battery = 50;
                CPU = 50;
                RAM = 50;
                Storage = 50;
                Pictures = 10;
                break;

            case "Low":
            default:
                battery = 90;
                CPU = 10;
                RAM = 10;
                Storage = 15;
                Pictures = 0;
                break;
        }
    }

    protected void StimulateUser(int userId, int battery, int CPU, int RAM, int Storage, int Pictures)
    {
        try
        {
            using (NpgsqlConnection connection = new NpgsqlConnection(ConnectoinString))
            {
                try
                {
                    connection.Open();

                    /* Select User Phone name */
                    NpgsqlCommand command = new NpgsqlCommand(
                        @"SELECT phone_name FROM users WHERE id = @userid;",
                        connection);
                    command.Parameters.AddWithValue("@userid", NpgsqlTypes.NpgsqlDbType.Integer, userId);
                    NpgsqlDataReader reader = command.ExecuteReader();
                    reader.Read();
                    string phoneName = reader[0].ToString();
                    reader.Close();
                    

                    DateTime dt = DateTime.Now;

                    

                    /* RAM */
                    command = new NpgsqlCommand(
                        @"INSERT INTO ram_usage(user_id,phone_name,value,insertion_time) VALUES(@userid,@name,@value,@date);",
                        connection);

                    command.Parameters.AddWithValue("@userid", NpgsqlTypes.NpgsqlDbType.Integer, userId);
                    command.Parameters.AddWithValue("@name", NpgsqlTypes.NpgsqlDbType.Char, phoneName);
                    command.Parameters.AddWithValue("@value", NpgsqlTypes.NpgsqlDbType.Double, (double)RAM);
                    command.Parameters.AddWithValue("@date", NpgsqlTypes.NpgsqlDbType.TimestampTZ, dt);
                    command.ExecuteNonQuery();

                    /* phoneusage */
                    command = new NpgsqlCommand(
                        @"INSERT INTO phoneusage(user_id,battery_usage,idle_time,apps_usage,time_stamp) VALUES(@userid,@battery,@idle,@apps,@date);",
                        connection);

                    command.Parameters.AddWithValue("@userid", NpgsqlTypes.NpgsqlDbType.Integer, userId);
                    command.Parameters.AddWithValue("@battery", NpgsqlTypes.NpgsqlDbType.Integer, battery);
                    command.Parameters.AddWithValue("@idle", NpgsqlTypes.NpgsqlDbType.Integer, CPU);
                    command.Parameters.AddWithValue("@apps", NpgsqlTypes.NpgsqlDbType.Char, "");
                    command.Parameters.AddWithValue("@date", NpgsqlTypes.NpgsqlDbType.TimestampTZ, dt);
                    command.ExecuteNonQuery();

                    /* battery_usage */
                    command = new NpgsqlCommand(
                        @"INSERT INTO battery_usage(user_id,phone_name,value,insertion_time) VALUES(@userid,@name,@value,@date);",
                        connection);

                    command.Parameters.AddWithValue("@userid", NpgsqlTypes.NpgsqlDbType.Integer, userId);
                    command.Parameters.AddWithValue("@name", NpgsqlTypes.NpgsqlDbType.Char, phoneName);
                    command.Parameters.AddWithValue("@value", NpgsqlTypes.NpgsqlDbType.Double, (double)battery);
                    command.Parameters.AddWithValue("@date", NpgsqlTypes.NpgsqlDbType.TimestampTZ, dt);
                    command.ExecuteNonQuery();

                    /* cpu_usage */
                    command = new NpgsqlCommand(
                        @"INSERT INTO cpu_usage(user_id,phone_name,value,insertion_time) VALUES(@userid,@name,@value,@date);",
                        connection);

                    command.Parameters.AddWithValue("@userid", NpgsqlTypes.NpgsqlDbType.Integer, userId);
                    command.Parameters.AddWithValue("@name", NpgsqlTypes.NpgsqlDbType.Char, phoneName);
                    command.Parameters.AddWithValue("@value", NpgsqlTypes.NpgsqlDbType.Double, (double)CPU);
                    command.Parameters.AddWithValue("@date", NpgsqlTypes.NpgsqlDbType.TimestampTZ, dt);
                    command.ExecuteNonQuery();

                    /* storage_usage */
                    command = new NpgsqlCommand(
                        @"INSERT INTO storage_usage(user_id,phone_name,free_storage,total_storage,insertion_time) VALUES(@userid,@name,@free,50,@date);",
                        connection);

                    command.Parameters.AddWithValue("@userid", NpgsqlTypes.NpgsqlDbType.Integer, userId);
                    command.Parameters.AddWithValue("@name", NpgsqlTypes.NpgsqlDbType.Char, phoneName);
                    command.Parameters.AddWithValue("@free", NpgsqlTypes.NpgsqlDbType.Double, (double)(int)((double)Storage/2));
                    command.Parameters.AddWithValue("@date", NpgsqlTypes.NpgsqlDbType.TimestampTZ, dt);
                    command.ExecuteNonQuery();

                    /* applications_usage */
                    command = new NpgsqlCommand(
                        @"INSERT INTO applications_usage(user_id,phone_name,value,insertion_time) VALUES(@userid,@name,@value,@date);",
                        connection);

                    command.Parameters.AddWithValue("@userid", NpgsqlTypes.NpgsqlDbType.Integer, userId);
                    command.Parameters.AddWithValue("@name", NpgsqlTypes.NpgsqlDbType.Char, phoneName);
                    command.Parameters.AddWithValue("@value", NpgsqlTypes.NpgsqlDbType.Text, "");
                    command.Parameters.AddWithValue("@date", NpgsqlTypes.NpgsqlDbType.TimestampTZ, dt);
                    command.ExecuteNonQuery();

                    connection.Close();
                }
                catch (Exception e)
                {
                }
            }
        }
        catch (Exception e)
        {
        }
    }

    protected void DeteleUser(int userId)
    {


        if(userId != 0)
        {
            try
            {
                using(NpgsqlConnection connection = new NpgsqlConnection(ConnectoinString))
                {
                    try
                    {
                        connection.Open();

                        /* RAM */
                    NpgsqlCommand command = new NpgsqlCommand(
                            @"DELETE FROM ram_usage WHERE user_id = @userid;",
                            connection);

                        command.Parameters.AddWithValue("@userid", NpgsqlTypes.NpgsqlDbType.Integer, userId);
                        command.ExecuteNonQuery();

                        /* phoneusage */
                        command = new NpgsqlCommand(
                            @"DELETE FROM phoneusage WHERE user_id = @userid;",
                            connection);

                        command.Parameters.AddWithValue("@userid", NpgsqlTypes.NpgsqlDbType.Integer, userId);
                        command.ExecuteNonQuery();

                        /* battery_usage */
                        command = new NpgsqlCommand(
                            @"DELETE FROM battery_usage WHERE user_id = @userid;",
                            connection);

                        command.Parameters.AddWithValue("@userid", NpgsqlTypes.NpgsqlDbType.Integer, userId);
                        command.ExecuteNonQuery();

                        /* cpu_usage */
                        command = new NpgsqlCommand(
                            @"DELETE FROM cpu_usage WHERE user_id = @userid;",
                            connection);

                        command.Parameters.AddWithValue("@userid", NpgsqlTypes.NpgsqlDbType.Integer, userId);
                        command.ExecuteNonQuery();

                        /* storage_usage */
                        command = new NpgsqlCommand(
                            @"DELETE FROM storage_usage WHERE user_id = @userid;",
                            connection);

                        command.Parameters.AddWithValue("@userid", NpgsqlTypes.NpgsqlDbType.Integer, userId);
                        command.ExecuteNonQuery();

                        /* applications_usage */
                        command = new NpgsqlCommand(
                            @"DELETE FROM applications_usage WHERE user_id = @userid;",
                            connection);

                        command.Parameters.AddWithValue("@userid", NpgsqlTypes.NpgsqlDbType.Integer, userId);
                        command.ExecuteNonQuery();

                        connection.Close();
                    }
                    catch(Exception)
                    {
                    }
                }
            }
            catch(Exception)
            {
            }
        }
    }
}
 
 
 
 
 