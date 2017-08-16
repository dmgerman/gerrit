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
DECL|package|com.google.gerrit.extensions.common
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|common
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
name|client
operator|.
name|AccountFieldName
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
name|AuthType
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
name|GitBasicAuthPolicy
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

begin_class
DECL|class|AuthInfo
specifier|public
class|class
name|AuthInfo
block|{
DECL|field|authType
specifier|public
name|AuthType
name|authType
decl_stmt|;
DECL|field|useContributorAgreements
specifier|public
name|Boolean
name|useContributorAgreements
decl_stmt|;
DECL|field|contributorAgreements
specifier|public
name|List
argument_list|<
name|AgreementInfo
argument_list|>
name|contributorAgreements
decl_stmt|;
DECL|field|editableAccountFields
specifier|public
name|List
argument_list|<
name|AccountFieldName
argument_list|>
name|editableAccountFields
decl_stmt|;
DECL|field|loginUrl
specifier|public
name|String
name|loginUrl
decl_stmt|;
DECL|field|loginText
specifier|public
name|String
name|loginText
decl_stmt|;
DECL|field|switchAccountUrl
specifier|public
name|String
name|switchAccountUrl
decl_stmt|;
DECL|field|registerUrl
specifier|public
name|String
name|registerUrl
decl_stmt|;
DECL|field|registerText
specifier|public
name|String
name|registerText
decl_stmt|;
DECL|field|editFullNameUrl
specifier|public
name|String
name|editFullNameUrl
decl_stmt|;
DECL|field|httpPasswordUrl
specifier|public
name|String
name|httpPasswordUrl
decl_stmt|;
DECL|field|gitBasicAuthPolicy
specifier|public
name|GitBasicAuthPolicy
name|gitBasicAuthPolicy
decl_stmt|;
block|}
end_class

end_unit

