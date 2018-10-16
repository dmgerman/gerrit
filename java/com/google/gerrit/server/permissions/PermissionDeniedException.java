begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.permissions
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|permissions
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
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
name|extensions
operator|.
name|api
operator|.
name|access
operator|.
name|GerritPermission
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
name|extensions
operator|.
name|restapi
operator|.
name|AuthException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_comment
comment|/**  * This signals that some permission check failed. The message is short so it can print on a  * single-line in the Git output.  */
end_comment

begin_class
DECL|class|PermissionDeniedException
specifier|public
class|class
name|PermissionDeniedException
extends|extends
name|AuthException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|MESSAGE_PREFIX
specifier|public
specifier|static
specifier|final
name|String
name|MESSAGE_PREFIX
init|=
literal|"not permitted: "
decl_stmt|;
DECL|field|permission
specifier|private
specifier|final
name|GerritPermission
name|permission
decl_stmt|;
DECL|field|resource
specifier|private
specifier|final
name|Optional
argument_list|<
name|String
argument_list|>
name|resource
decl_stmt|;
DECL|method|PermissionDeniedException (GerritPermission permission)
specifier|public
name|PermissionDeniedException
parameter_list|(
name|GerritPermission
name|permission
parameter_list|)
block|{
name|super
argument_list|(
name|MESSAGE_PREFIX
operator|+
name|requireNonNull
argument_list|(
name|permission
argument_list|)
operator|.
name|describeForException
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|permission
operator|=
name|permission
expr_stmt|;
name|this
operator|.
name|resource
operator|=
name|Optional
operator|.
name|empty
argument_list|()
expr_stmt|;
block|}
DECL|method|PermissionDeniedException (GerritPermission permission, String resource)
specifier|public
name|PermissionDeniedException
parameter_list|(
name|GerritPermission
name|permission
parameter_list|,
name|String
name|resource
parameter_list|)
block|{
name|super
argument_list|(
name|MESSAGE_PREFIX
operator|+
name|requireNonNull
argument_list|(
name|permission
argument_list|)
operator|.
name|describeForException
argument_list|()
operator|+
literal|" on "
operator|+
name|requireNonNull
argument_list|(
name|resource
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|permission
operator|=
name|permission
expr_stmt|;
name|this
operator|.
name|resource
operator|=
name|Optional
operator|.
name|of
argument_list|(
name|resource
argument_list|)
expr_stmt|;
block|}
DECL|method|describePermission ()
specifier|public
name|String
name|describePermission
parameter_list|()
block|{
return|return
name|permission
operator|.
name|describeForException
argument_list|()
return|;
block|}
DECL|method|getResource ()
specifier|public
name|Optional
argument_list|<
name|String
argument_list|>
name|getResource
parameter_list|()
block|{
return|return
name|resource
return|;
block|}
block|}
end_class

end_unit

