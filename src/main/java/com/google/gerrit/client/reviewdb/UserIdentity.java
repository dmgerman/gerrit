begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.reviewdb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|reviewdb
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_class
DECL|class|UserIdentity
specifier|public
specifier|final
class|class
name|UserIdentity
block|{
comment|/** Full name of the user. */
DECL|field|name
specifier|protected
name|String
name|name
decl_stmt|;
comment|/** Email address (or user@host style string anyway). */
DECL|field|email
specifier|protected
name|String
name|email
decl_stmt|;
comment|/** Time (in UTC) when the identity was constructed. */
DECL|field|when
specifier|protected
name|Timestamp
name|when
decl_stmt|;
comment|/** Offset from UTC */
DECL|field|tz
specifier|protected
name|int
name|tz
decl_stmt|;
comment|/** If the user has a Gerrit account, their account identity. */
DECL|field|accountId
specifier|protected
name|Account
operator|.
name|Id
name|accountId
decl_stmt|;
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|setName (final String n)
specifier|public
name|void
name|setName
parameter_list|(
specifier|final
name|String
name|n
parameter_list|)
block|{
name|name
operator|=
name|n
expr_stmt|;
block|}
DECL|method|getEmail ()
specifier|public
name|String
name|getEmail
parameter_list|()
block|{
return|return
name|email
return|;
block|}
DECL|method|setEmail (final String e)
specifier|public
name|void
name|setEmail
parameter_list|(
specifier|final
name|String
name|e
parameter_list|)
block|{
name|email
operator|=
name|e
expr_stmt|;
block|}
DECL|method|getDate ()
specifier|public
name|Timestamp
name|getDate
parameter_list|()
block|{
return|return
name|when
return|;
block|}
DECL|method|setDate (final Timestamp d)
specifier|public
name|void
name|setDate
parameter_list|(
specifier|final
name|Timestamp
name|d
parameter_list|)
block|{
name|when
operator|=
name|d
expr_stmt|;
block|}
DECL|method|getTimeZone ()
specifier|public
name|int
name|getTimeZone
parameter_list|()
block|{
return|return
name|tz
return|;
block|}
DECL|method|setTimeZone (final int offset)
specifier|public
name|void
name|setTimeZone
parameter_list|(
specifier|final
name|int
name|offset
parameter_list|)
block|{
name|tz
operator|=
name|offset
expr_stmt|;
block|}
DECL|method|getAccount ()
specifier|public
name|Account
operator|.
name|Id
name|getAccount
parameter_list|()
block|{
return|return
name|accountId
return|;
block|}
DECL|method|setAccount (final Account.Id id)
specifier|public
name|void
name|setAccount
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
name|accountId
operator|=
name|id
expr_stmt|;
block|}
block|}
end_class

end_unit

