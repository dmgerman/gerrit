begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
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
name|extensions
operator|.
name|common
operator|.
name|AccountInfo
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|permissions
operator|.
name|PermissionBackendException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * Directory of user account information.  *  *<p>Implementations supply data to Gerrit about user accounts.  */
end_comment

begin_class
DECL|class|AccountDirectory
specifier|public
specifier|abstract
class|class
name|AccountDirectory
block|{
comment|/** Fields to be populated for a REST API response. */
DECL|enum|FillOptions
specifier|public
enum|enum
name|FillOptions
block|{
comment|/** Human friendly display name presented in the web interface. */
DECL|enumConstant|NAME
name|NAME
block|,
comment|/** Preferred email address to contact the user at. */
DECL|enumConstant|EMAIL
name|EMAIL
block|,
comment|/** All secondary email addresses of the user. */
DECL|enumConstant|SECONDARY_EMAILS
name|SECONDARY_EMAILS
block|,
comment|/** User profile images. */
DECL|enumConstant|AVATARS
name|AVATARS
block|,
comment|/** Unique user identity to login to Gerrit, may be deprecated. */
DECL|enumConstant|USERNAME
name|USERNAME
block|,
comment|/** Numeric account ID, may be deprecated. */
DECL|enumConstant|ID
name|ID
block|,
comment|/** The user-settable status of this account (e.g. busy, OOO, available) */
DECL|enumConstant|STATUS
name|STATUS
block|}
DECL|method|fillAccountInfo (Iterable<? extends AccountInfo> in, Set<FillOptions> options)
specifier|public
specifier|abstract
name|void
name|fillAccountInfo
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|AccountInfo
argument_list|>
name|in
parameter_list|,
name|Set
argument_list|<
name|FillOptions
argument_list|>
name|options
parameter_list|)
throws|throws
name|PermissionBackendException
function_decl|;
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
block|}
end_class

end_unit

