begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|Gerrit
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|client
operator|.
name|CookieAccess
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|server
operator|.
name|ActiveCall
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|server
operator|.
name|ValidToken
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|server
operator|.
name|XsrfException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|Cookie
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
import|;
end_import

begin_class
DECL|class|GerritCall
specifier|public
class|class
name|GerritCall
extends|extends
name|ActiveCall
block|{
DECL|field|server
specifier|private
specifier|final
name|GerritServer
name|server
decl_stmt|;
DECL|field|readAccountCookie
specifier|private
name|boolean
name|readAccountCookie
decl_stmt|;
DECL|field|accountCookie
specifier|private
name|ValidToken
name|accountCookie
decl_stmt|;
DECL|method|GerritCall (final GerritServer gs, final HttpServletRequest i, final HttpServletResponse o)
specifier|public
name|GerritCall
parameter_list|(
specifier|final
name|GerritServer
name|gs
parameter_list|,
specifier|final
name|HttpServletRequest
name|i
parameter_list|,
specifier|final
name|HttpServletResponse
name|o
parameter_list|)
block|{
name|super
argument_list|(
name|i
argument_list|,
name|o
argument_list|)
expr_stmt|;
name|server
operator|=
name|gs
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getUser ()
specifier|public
name|String
name|getUser
parameter_list|()
block|{
if|if
condition|(
operator|!
name|readAccountCookie
condition|)
block|{
name|readAccountCookie
operator|=
literal|true
expr_stmt|;
name|String
name|idstr
init|=
name|CookieAccess
operator|.
name|get
argument_list|(
name|Gerrit
operator|.
name|ACCOUNT_COOKIE
argument_list|)
decl_stmt|;
try|try
block|{
name|accountCookie
operator|=
name|server
operator|.
name|getAccountToken
argument_list|()
operator|.
name|checkToken
argument_list|(
name|idstr
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XsrfException
name|e
parameter_list|)
block|{
name|accountCookie
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|accountCookie
operator|!=
literal|null
operator|&&
name|accountCookie
operator|.
name|needsRefresh
argument_list|()
condition|)
block|{
try|try
block|{
name|idstr
operator|=
name|server
operator|.
name|getAccountToken
argument_list|()
operator|.
name|newToken
argument_list|(
name|accountCookie
operator|.
name|getData
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Cookie
name|c
init|=
operator|new
name|Cookie
argument_list|(
name|Gerrit
operator|.
name|ACCOUNT_COOKIE
argument_list|,
name|idstr
argument_list|)
decl_stmt|;
name|c
operator|.
name|setMaxAge
argument_list|(
name|server
operator|.
name|getSessionAge
argument_list|()
argument_list|)
expr_stmt|;
name|c
operator|.
name|setPath
argument_list|(
name|getHttpServletRequest
argument_list|()
operator|.
name|getContextPath
argument_list|()
argument_list|)
expr_stmt|;
name|getHttpServletResponse
argument_list|()
operator|.
name|addCookie
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XsrfException
name|e
parameter_list|)
block|{         }
block|}
block|}
return|return
name|accountCookie
operator|!=
literal|null
condition|?
name|accountCookie
operator|.
name|getData
argument_list|()
else|:
literal|null
return|;
block|}
block|}
end_class

end_unit

