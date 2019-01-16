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
DECL|package|com.google.gerrit.server.restapi.access
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|restapi
operator|.
name|access
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
name|exceptions
operator|.
name|StorageException
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
name|ProjectAccessInfo
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
name|ResourceConflictException
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
name|ResourceNotFoundException
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
name|RestReadView
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
name|TopLevelResource
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
name|reviewdb
operator|.
name|client
operator|.
name|Project
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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|restapi
operator|.
name|project
operator|.
name|GetAccess
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|Option
import|;
end_import

begin_class
DECL|class|ListAccess
specifier|public
class|class
name|ListAccess
implements|implements
name|RestReadView
argument_list|<
name|TopLevelResource
argument_list|>
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--project"
argument_list|,
name|aliases
operator|=
block|{
literal|"-p"
block|}
argument_list|,
name|metaVar
operator|=
literal|"PROJECT"
argument_list|,
name|usage
operator|=
literal|"projects for which the access rights should be returned"
argument_list|)
DECL|field|projects
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|projects
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|getAccess
specifier|private
specifier|final
name|GetAccess
name|getAccess
decl_stmt|;
annotation|@
name|Inject
DECL|method|ListAccess (GetAccess getAccess)
specifier|public
name|ListAccess
parameter_list|(
name|GetAccess
name|getAccess
parameter_list|)
block|{
name|this
operator|.
name|getAccess
operator|=
name|getAccess
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (TopLevelResource resource)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|ProjectAccessInfo
argument_list|>
name|apply
parameter_list|(
name|TopLevelResource
name|resource
parameter_list|)
throws|throws
name|ResourceNotFoundException
throws|,
name|ResourceConflictException
throws|,
name|IOException
throws|,
name|PermissionBackendException
throws|,
name|StorageException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|ProjectAccessInfo
argument_list|>
name|access
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|p
range|:
name|projects
control|)
block|{
name|access
operator|.
name|put
argument_list|(
name|p
argument_list|,
name|getAccess
operator|.
name|apply
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|p
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|access
return|;
block|}
block|}
end_class

end_unit

