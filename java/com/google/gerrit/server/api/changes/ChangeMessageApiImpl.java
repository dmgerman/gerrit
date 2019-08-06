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
DECL|package|com.google.gerrit.server.api.changes
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
name|changes
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
name|ChangeMessageApi
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
name|DeleteChangeMessageInput
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
name|common
operator|.
name|ChangeMessageInfo
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
name|ChangeMessageResource
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
name|change
operator|.
name|DeleteChangeMessage
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
name|change
operator|.
name|GetChangeMessage
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
DECL|class|ChangeMessageApiImpl
class|class
name|ChangeMessageApiImpl
implements|implements
name|ChangeMessageApi
block|{
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create (ChangeMessageResource changeMessageResource)
name|ChangeMessageApiImpl
name|create
parameter_list|(
name|ChangeMessageResource
name|changeMessageResource
parameter_list|)
function_decl|;
block|}
DECL|field|getChangeMessage
specifier|private
specifier|final
name|GetChangeMessage
name|getChangeMessage
decl_stmt|;
DECL|field|deleteChangeMessage
specifier|private
specifier|final
name|DeleteChangeMessage
name|deleteChangeMessage
decl_stmt|;
DECL|field|changeMessageResource
specifier|private
specifier|final
name|ChangeMessageResource
name|changeMessageResource
decl_stmt|;
annotation|@
name|Inject
DECL|method|ChangeMessageApiImpl ( GetChangeMessage getChangeMessage, DeleteChangeMessage deleteChangeMessage, @Assisted ChangeMessageResource changeMessageResource)
name|ChangeMessageApiImpl
parameter_list|(
name|GetChangeMessage
name|getChangeMessage
parameter_list|,
name|DeleteChangeMessage
name|deleteChangeMessage
parameter_list|,
annotation|@
name|Assisted
name|ChangeMessageResource
name|changeMessageResource
parameter_list|)
block|{
name|this
operator|.
name|getChangeMessage
operator|=
name|getChangeMessage
expr_stmt|;
name|this
operator|.
name|deleteChangeMessage
operator|=
name|deleteChangeMessage
expr_stmt|;
name|this
operator|.
name|changeMessageResource
operator|=
name|changeMessageResource
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|ChangeMessageInfo
name|get
parameter_list|()
throws|throws
name|RestApiException
block|{
try|try
block|{
return|return
name|getChangeMessage
operator|.
name|apply
argument_list|(
name|changeMessageResource
argument_list|)
operator|.
name|value
argument_list|()
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
literal|"Cannot retrieve change message"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|delete (DeleteChangeMessageInput input)
specifier|public
name|ChangeMessageInfo
name|delete
parameter_list|(
name|DeleteChangeMessageInput
name|input
parameter_list|)
throws|throws
name|RestApiException
block|{
try|try
block|{
return|return
name|deleteChangeMessage
operator|.
name|apply
argument_list|(
name|changeMessageResource
argument_list|,
name|input
argument_list|)
operator|.
name|value
argument_list|()
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
literal|"Cannot delete change message"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

