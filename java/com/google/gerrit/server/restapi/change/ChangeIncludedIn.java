begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.restapi.change
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
name|change
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
name|entities
operator|.
name|PatchSet
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
name|IncludedInInfo
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
name|PatchSetUtil
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
name|ChangeResource
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
name|IncludedIn
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
name|Singleton
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

begin_class
annotation|@
name|Singleton
DECL|class|ChangeIncludedIn
specifier|public
class|class
name|ChangeIncludedIn
implements|implements
name|RestReadView
argument_list|<
name|ChangeResource
argument_list|>
block|{
DECL|field|psUtil
specifier|private
name|PatchSetUtil
name|psUtil
decl_stmt|;
DECL|field|includedIn
specifier|private
name|IncludedIn
name|includedIn
decl_stmt|;
annotation|@
name|Inject
DECL|method|ChangeIncludedIn (PatchSetUtil psUtil, IncludedIn includedIn)
name|ChangeIncludedIn
parameter_list|(
name|PatchSetUtil
name|psUtil
parameter_list|,
name|IncludedIn
name|includedIn
parameter_list|)
block|{
name|this
operator|.
name|psUtil
operator|=
name|psUtil
expr_stmt|;
name|this
operator|.
name|includedIn
operator|=
name|includedIn
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ChangeResource rsrc)
specifier|public
name|Response
argument_list|<
name|IncludedInInfo
argument_list|>
name|apply
parameter_list|(
name|ChangeResource
name|rsrc
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|IOException
block|{
name|PatchSet
name|ps
init|=
name|psUtil
operator|.
name|current
argument_list|(
name|rsrc
operator|.
name|getNotes
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|includedIn
operator|.
name|apply
argument_list|(
name|rsrc
operator|.
name|getProject
argument_list|()
argument_list|,
name|ps
operator|.
name|commitId
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

