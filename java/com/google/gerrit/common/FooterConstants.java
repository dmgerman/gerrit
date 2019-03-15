begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.common
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
package|;
end_package

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|revwalk
operator|.
name|FooterKey
import|;
end_import

begin_class
DECL|class|FooterConstants
specifier|public
class|class
name|FooterConstants
block|{
comment|/** The change ID as used to track patch sets. */
DECL|field|CHANGE_ID
specifier|public
specifier|static
specifier|final
name|FooterKey
name|CHANGE_ID
init|=
operator|new
name|FooterKey
argument_list|(
literal|"Change-Id"
argument_list|)
decl_stmt|;
comment|/** The footer telling us who reviewed the change. */
DECL|field|REVIEWED_BY
specifier|public
specifier|static
specifier|final
name|FooterKey
name|REVIEWED_BY
init|=
operator|new
name|FooterKey
argument_list|(
literal|"Reviewed-by"
argument_list|)
decl_stmt|;
comment|/** The footer telling us the URL where the review took place. */
DECL|field|REVIEWED_ON
specifier|public
specifier|static
specifier|final
name|FooterKey
name|REVIEWED_ON
init|=
operator|new
name|FooterKey
argument_list|(
literal|"Reviewed-on"
argument_list|)
decl_stmt|;
comment|/** The footer telling us who tested the change. */
DECL|field|TESTED_BY
specifier|public
specifier|static
specifier|final
name|FooterKey
name|TESTED_BY
init|=
operator|new
name|FooterKey
argument_list|(
literal|"Tested-by"
argument_list|)
decl_stmt|;
block|}
end_class

end_unit

