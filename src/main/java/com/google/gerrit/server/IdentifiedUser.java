begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2009 Google Inc. All Rights Reserved.
end_comment

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
name|reviewdb
operator|.
name|Account
import|;
end_import

begin_comment
comment|/** An authenticated user. */
end_comment

begin_class
DECL|class|IdentifiedUser
specifier|public
class|class
name|IdentifiedUser
extends|extends
name|CurrentUser
block|{
DECL|field|accountId
specifier|private
specifier|final
name|Account
operator|.
name|Id
name|accountId
decl_stmt|;
DECL|method|IdentifiedUser (final Account.Id i)
specifier|public
name|IdentifiedUser
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|i
parameter_list|)
block|{
name|accountId
operator|=
name|i
expr_stmt|;
block|}
comment|/** The account identity for the user. */
DECL|method|getAccountId ()
specifier|public
name|Account
operator|.
name|Id
name|getAccountId
parameter_list|()
block|{
return|return
name|accountId
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"CurrentUser["
operator|+
name|getAccountId
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

