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
name|common
operator|.
name|Nullable
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
name|PureRevertInfo
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
name|BadRequestException
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
name|PureRevert
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
name|Optional
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
DECL|class|GetPureRevert
specifier|public
class|class
name|GetPureRevert
implements|implements
name|RestReadView
argument_list|<
name|ChangeResource
argument_list|>
block|{
DECL|field|pureRevert
specifier|private
specifier|final
name|PureRevert
name|pureRevert
decl_stmt|;
DECL|field|claimedOriginal
annotation|@
name|Nullable
specifier|private
name|String
name|claimedOriginal
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--claimed-original"
argument_list|,
name|aliases
operator|=
block|{
literal|"-o"
block|}
argument_list|,
name|usage
operator|=
literal|"SHA1 (40 digit hex) of the original commit"
argument_list|)
DECL|method|setClaimedOriginal (String claimedOriginal)
specifier|public
name|void
name|setClaimedOriginal
parameter_list|(
name|String
name|claimedOriginal
parameter_list|)
block|{
name|this
operator|.
name|claimedOriginal
operator|=
name|claimedOriginal
expr_stmt|;
block|}
annotation|@
name|Inject
DECL|method|GetPureRevert (PureRevert pureRevert)
name|GetPureRevert
parameter_list|(
name|PureRevert
name|pureRevert
parameter_list|)
block|{
name|this
operator|.
name|pureRevert
operator|=
name|pureRevert
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ChangeResource rsrc)
specifier|public
name|PureRevertInfo
name|apply
parameter_list|(
name|ChangeResource
name|rsrc
parameter_list|)
throws|throws
name|ResourceConflictException
throws|,
name|IOException
throws|,
name|BadRequestException
throws|,
name|AuthException
block|{
name|boolean
name|isPureRevert
init|=
name|pureRevert
operator|.
name|get
argument_list|(
name|rsrc
operator|.
name|getNotes
argument_list|()
argument_list|,
name|Optional
operator|.
name|ofNullable
argument_list|(
name|claimedOriginal
argument_list|)
argument_list|)
decl_stmt|;
return|return
operator|new
name|PureRevertInfo
argument_list|(
name|isPureRevert
argument_list|)
return|;
block|}
block|}
end_class

end_unit

