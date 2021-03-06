/*
 * Copyright (C) 2015 47 Degrees, LLC http://47deg.com hello@47deg.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may
 *  not use this file except in compliance with the License. You may obtain
 *  a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.fortysevendeg.android.scaladays.ui.speakers

import android.support.v7.widget.RecyclerView
import android.view.View.OnClickListener
import android.view.{View, ViewGroup}
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.model.Speaker
import com.fortysevendeg.android.scaladays.ui.commons.AsyncImageTweaks._
import macroid.extras.ImageViewTweaks._
import macroid.extras.TextViewTweaks._
import macroid.extras.ViewTweaks._
import macroid._
import macroid.ActivityContextWrapper

class SpeakersAdapter(speakers: Seq[Speaker], listener: RecyclerClickListener)
    (implicit context: ActivityContextWrapper)
    extends RecyclerView.Adapter[ViewHolderSpeakersAdapter] {

  val recyclerClickListener: RecyclerClickListener = listener

  override def onCreateViewHolder(parentViewGroup: ViewGroup, i: Int): ViewHolderSpeakersAdapter = {
    val adapter = new SpeakersLayoutAdapter()
    adapter.content.setOnClickListener(new OnClickListener {
      override def onClick(v: View): Unit = recyclerClickListener.onClick(speakers(v.getTag.asInstanceOf[Int]))
    })
    new ViewHolderSpeakersAdapter(adapter)
  }

  override def getItemCount: Int = speakers.size

  override def onBindViewHolder(viewHolder: ViewHolderSpeakersAdapter, position: Int): Unit = {
    val speaker = speakers(position)
    val avatarSize = context.application.getResources.getDimensionPixelSize(R.dimen.size_avatar)
    viewHolder.content.setTag(position)
    Ui.run(
      (viewHolder.avatar <~
        (speaker.picture map {
          roundedImage(_, R.drawable.placeholder_circle, avatarSize, Some(R.drawable.placeholder_avatar_failed))
        } getOrElse ivSrc(R.drawable.placeholder_avatar_failed))) ~
          (viewHolder.name <~ tvText(speaker.name)) ~
          (viewHolder.twitter <~ speaker.twitter.map(tvText(_) + vVisible).getOrElse(vGone)) ~
          (viewHolder.bio <~ tvText(speaker.bio))
    )
  }
}

trait RecyclerClickListener {
  def onClick(speaker: Speaker)
}