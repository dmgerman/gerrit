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
DECL|package|com.google.gerrit.pgm.init
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
operator|.
name|init
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Objects
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Strings
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
name|config
operator|.
name|AllProjectsNameProvider
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
name|Provider
import|;
end_import

begin_class
DECL|class|AllProjectsNameOnInitProvider
specifier|public
class|class
name|AllProjectsNameOnInitProvider
implements|implements
name|Provider
argument_list|<
name|String
argument_list|>
block|{
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
annotation|@
name|Inject
DECL|method|AllProjectsNameOnInitProvider (Section.Factory sections)
name|AllProjectsNameOnInitProvider
parameter_list|(
name|Section
operator|.
name|Factory
name|sections
parameter_list|)
block|{
name|String
name|n
init|=
name|sections
operator|.
name|get
argument_list|(
literal|"gerrit"
argument_list|,
literal|null
argument_list|)
operator|.
name|get
argument_list|(
literal|"allProjects"
argument_list|)
decl_stmt|;
name|name
operator|=
name|Objects
operator|.
name|firstNonNull
argument_list|(
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|n
argument_list|)
argument_list|,
name|AllProjectsNameProvider
operator|.
name|DEFAULT
argument_list|)
expr_stmt|;
block|}
DECL|method|get ()
specifier|public
name|String
name|get
parameter_list|()
block|{
return|return
name|name
return|;
block|}
block|}
end_class

end_unit

