begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
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

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|assistedinject
operator|.
name|Assisted
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|servlet
operator|.
name|RequestScoped
import|;
end_import

begin_comment
comment|/** An authenticated user. */
end_comment

begin_class
annotation|@
name|RequestScoped
DECL|class|IdentifiedUser
specifier|public
class|class
name|IdentifiedUser
extends|extends
name|CurrentUser
block|{
DECL|class|Factory
specifier|public
specifier|static
specifier|final
class|class
name|Factory
block|{
DECL|method|create (final Account.Id id)
specifier|public
name|IdentifiedUser
name|create
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
return|return
operator|new
name|IdentifiedUser
argument_list|(
name|id
argument_list|)
return|;
block|}
block|}
DECL|field|accountId
specifier|private
specifier|final
name|Account
operator|.
name|Id
name|accountId
decl_stmt|;
annotation|@
name|Inject
DECL|method|IdentifiedUser (@ssisted final Account.Id i)
name|IdentifiedUser
parameter_list|(
annotation|@
name|Assisted
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

