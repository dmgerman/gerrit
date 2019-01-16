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
name|client
operator|.
name|ListChangesOption
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
name|ChangeInfo
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
name|DynamicOptions
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
name|DynamicOptions
operator|.
name|DynamicBean
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
DECL|class|GetDetail
specifier|public
class|class
name|GetDetail
implements|implements
name|RestReadView
argument_list|<
name|ChangeResource
argument_list|>
implements|,
name|DynamicOptions
operator|.
name|BeanReceiver
block|{
DECL|field|delegate
specifier|private
specifier|final
name|GetChange
name|delegate
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-o"
argument_list|,
name|usage
operator|=
literal|"Output options"
argument_list|)
DECL|method|addOption (ListChangesOption o)
name|void
name|addOption
parameter_list|(
name|ListChangesOption
name|o
parameter_list|)
block|{
name|delegate
operator|.
name|addOption
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-O"
argument_list|,
name|usage
operator|=
literal|"Output option flags, in hex"
argument_list|)
DECL|method|setOptionFlagsHex (String hex)
name|void
name|setOptionFlagsHex
parameter_list|(
name|String
name|hex
parameter_list|)
block|{
name|delegate
operator|.
name|setOptionFlagsHex
argument_list|(
name|hex
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Inject
DECL|method|GetDetail (GetChange delegate)
name|GetDetail
parameter_list|(
name|GetChange
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
name|delegate
operator|.
name|addOption
argument_list|(
name|ListChangesOption
operator|.
name|LABELS
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|addOption
argument_list|(
name|ListChangesOption
operator|.
name|DETAILED_LABELS
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|addOption
argument_list|(
name|ListChangesOption
operator|.
name|DETAILED_ACCOUNTS
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|addOption
argument_list|(
name|ListChangesOption
operator|.
name|MESSAGES
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|addOption
argument_list|(
name|ListChangesOption
operator|.
name|REVIEWER_UPDATES
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setDynamicBean (String plugin, DynamicBean dynamicBean)
specifier|public
name|void
name|setDynamicBean
parameter_list|(
name|String
name|plugin
parameter_list|,
name|DynamicBean
name|dynamicBean
parameter_list|)
block|{
name|delegate
operator|.
name|setDynamicBean
argument_list|(
name|plugin
argument_list|,
name|dynamicBean
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getExportedBeanReceiver ()
specifier|public
name|Class
argument_list|<
name|?
extends|extends
name|DynamicOptions
operator|.
name|BeanReceiver
argument_list|>
name|getExportedBeanReceiver
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getExportedBeanReceiver
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|apply (ChangeResource rsrc)
specifier|public
name|Response
argument_list|<
name|ChangeInfo
argument_list|>
name|apply
parameter_list|(
name|ChangeResource
name|rsrc
parameter_list|)
throws|throws
name|StorageException
block|{
return|return
name|delegate
operator|.
name|apply
argument_list|(
name|rsrc
argument_list|)
return|;
block|}
block|}
end_class

end_unit

