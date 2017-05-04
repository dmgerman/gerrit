begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.api.projects
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|api
operator|.
name|projects
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|api
operator|.
name|ApiUtil
operator|.
name|asRestApiException
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
name|changes
operator|.
name|ChangeApi
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
name|changes
operator|.
name|Changes
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
name|changes
operator|.
name|CherryPickInput
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
name|projects
operator|.
name|CommitApi
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
name|RestApiException
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
name|change
operator|.
name|CherryPickCommit
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
name|CommitResource
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

begin_class
DECL|class|CommitApiImpl
specifier|public
class|class
name|CommitApiImpl
implements|implements
name|CommitApi
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (CommitResource r)
name|CommitApiImpl
name|create
parameter_list|(
name|CommitResource
name|r
parameter_list|)
function_decl|;
block|}
DECL|field|changes
specifier|private
specifier|final
name|Changes
name|changes
decl_stmt|;
DECL|field|cherryPickCommit
specifier|private
specifier|final
name|CherryPickCommit
name|cherryPickCommit
decl_stmt|;
DECL|field|commitResource
specifier|private
specifier|final
name|CommitResource
name|commitResource
decl_stmt|;
annotation|@
name|Inject
DECL|method|CommitApiImpl ( Changes changes, CherryPickCommit cherryPickCommit, @Assisted CommitResource commitResource)
name|CommitApiImpl
parameter_list|(
name|Changes
name|changes
parameter_list|,
name|CherryPickCommit
name|cherryPickCommit
parameter_list|,
annotation|@
name|Assisted
name|CommitResource
name|commitResource
parameter_list|)
block|{
name|this
operator|.
name|changes
operator|=
name|changes
expr_stmt|;
name|this
operator|.
name|cherryPickCommit
operator|=
name|cherryPickCommit
expr_stmt|;
name|this
operator|.
name|commitResource
operator|=
name|commitResource
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|cherryPick (CherryPickInput input)
specifier|public
name|ChangeApi
name|cherryPick
parameter_list|(
name|CherryPickInput
name|input
parameter_list|)
throws|throws
name|RestApiException
block|{
try|try
block|{
return|return
name|changes
operator|.
name|id
argument_list|(
name|cherryPickCommit
operator|.
name|apply
argument_list|(
name|commitResource
argument_list|,
name|input
argument_list|)
operator|.
name|_number
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|asRestApiException
argument_list|(
literal|"Cannot cherry pick"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

