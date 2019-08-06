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
DECL|package|com.google.gerrit.server.restapi.project
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
name|project
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
name|ProjectInfo
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
name|Response
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
name|server
operator|.
name|project
operator|.
name|ChildProjectResource
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
name|project
operator|.
name|ProjectJson
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
DECL|class|GetChildProject
specifier|public
class|class
name|GetChildProject
implements|implements
name|RestReadView
argument_list|<
name|ChildProjectResource
argument_list|>
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--recursive"
argument_list|,
name|usage
operator|=
literal|"to list child projects recursively"
argument_list|)
DECL|method|setRecursive (boolean recursive)
specifier|public
name|void
name|setRecursive
parameter_list|(
name|boolean
name|recursive
parameter_list|)
block|{
name|this
operator|.
name|recursive
operator|=
name|recursive
expr_stmt|;
block|}
DECL|field|json
specifier|private
specifier|final
name|ProjectJson
name|json
decl_stmt|;
DECL|field|recursive
specifier|private
name|boolean
name|recursive
decl_stmt|;
annotation|@
name|Inject
DECL|method|GetChildProject (ProjectJson json)
name|GetChildProject
parameter_list|(
name|ProjectJson
name|json
parameter_list|)
block|{
name|this
operator|.
name|json
operator|=
name|json
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ChildProjectResource rsrc)
specifier|public
name|Response
argument_list|<
name|ProjectInfo
argument_list|>
name|apply
parameter_list|(
name|ChildProjectResource
name|rsrc
parameter_list|)
throws|throws
name|ResourceNotFoundException
block|{
if|if
condition|(
name|recursive
operator|||
name|rsrc
operator|.
name|isDirectChild
argument_list|()
condition|)
block|{
return|return
name|Response
operator|.
name|ok
argument_list|(
name|json
operator|.
name|format
argument_list|(
name|rsrc
operator|.
name|getChild
argument_list|()
operator|.
name|getProject
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|rsrc
operator|.
name|getChild
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

