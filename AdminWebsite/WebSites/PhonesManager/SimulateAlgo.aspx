<%@ Page Title="" Language="C#" MasterPageFile="~/MasterPage.master" AutoEventWireup="true" CodeFile="SimulateAlgo.aspx.cs" Inherits="SimulateAlgo" %>
<asp:Content ID="Content1" ContentPlaceHolderID="title" Runat="Server">
    Home
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content3" ContentPlaceHolderID="contentBody" Runat="Server">    
        <asp:Label ID="Label1" runat="server" Text="Enter User Id:"></asp:Label>
        <asp:TextBox ID="txtUserID" runat="server"></asp:TextBox>
        <br />
        <br />
        <br />
        <asp:Label ID="Label2" runat="server" Text="Battery Grade:"></asp:Label>
        <asp:TextBox ID="txtBattery" runat="server"></asp:TextBox>
        <br />
        <br />
        <br />
        <asp:Label ID="Label3" runat="server" Text="CPU Grade:"></asp:Label>
        <asp:TextBox ID="txtCPU" runat="server"></asp:TextBox>
        <br />
        <br />
        <br />
        <asp:Label ID="Label4" runat="server" Text="RAM Grade:"></asp:Label>
        <asp:TextBox ID="txtRam" runat="server"></asp:TextBox>
        <br />
        <br />
        <br />
        <asp:Label ID="Label5" runat="server" Text="Storage Grade:"></asp:Label>
        <asp:TextBox ID="txtStorage" runat="server"></asp:TextBox>
        <br />
        <br />
        <br />
        <asp:Label ID="Label6" runat="server" Text="Camera Grade:"></asp:Label>
        <asp:TextBox ID="txtCamera" runat="server"></asp:TextBox>
        <br />
        <br />
        <br />
        <asp:Button ID="btnSubmit" runat="server" OnClick="btnSubmit_Click" Text="Simulate" />
        <br />
</asp:Content>
